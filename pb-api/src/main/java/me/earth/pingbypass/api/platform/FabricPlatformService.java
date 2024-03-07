package me.earth.pingbypass.api.platform;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.Transformer;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.fabricmc.loader.impl.launch.knot.Knot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.transformer.Config;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

@Slf4j
final class FabricPlatformService implements PlatformService {
    private final TransformingRegistry registry = new TransformingRegistry();
    private final AtomicBoolean injected = new AtomicBoolean();

    @Override
    public void addToClassPath(Path path) {
        FabricLauncherBase.getLauncher().addToClassPath(path);
    }

    @Override
    public Transformer.Registry injectTransformerRegistry() {
        synchronized (injected) {
            if (!injected.getAndSet(true)) {
                setMixinTransformer(transformer -> new TransformingRegistry.MixinTransformerRegistry(transformer, registry));
            }
        }

        return registry;
    }

    public void setMixinTransformer(UnaryOperator<IMixinTransformer> factory) {
        try {
            IMixinTransformer transformer = getTransformer();
            transformer = factory.apply(transformer);
            setTransformer(transformer);
        } catch (Exception e) {
            log.error("Failed to set MixinTransformer", e);
        }
    }

    /**
     * It seems that we cannot add MixinConfigs from Fabrics PreLaunchEntryPoint properly
     * at least when loading with Future client.
     * Because of that we load from an {@link org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin}
     * which causes other problems.
     * For those see {@link #loadMixinConfig(String)}.
     *
     * @param config the MixinConfig to add.
     */
    @Override
    public void addMixinConfig(String config) {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if ("org.spongepowered.asm.mixin.transformer.MixinProcessor".equals(element.getClassName()) && "selectConfigs".equals(element.getMethodName())) {
                log.info("Calling addMixinConfig during MixinProcessor.selectConfigs, using FabricPlatformService.loadMixinConfig to add " + config);
                loadMixinConfig(config);
                return;
            }
        }

        log.info("Loading MixinConfig " + config + " normally.");
        PlatformService.super.addMixinConfig(config);
    }

    /**
     * If we try to add a MixinConfig from an {@link org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin} we
     * might crash on fabric as the MixinConfigPlugin is loaded while iterating over the configs, because we get a
     * {@link java.util.ConcurrentModificationException} when adding a config during that time.
     *
     * @param configFile the name of the Mixin config file to load.
     * @see Mixins#addConfiguration(String)
     */
    @SneakyThrows
    @SuppressWarnings("deprecation")
    public static void loadMixinConfig(String configFile) {
        Config config = null;
        try {
            config = Config.create(configFile, MixinEnvironment.getDefaultEnvironment());
        } catch (Exception ex) {
            log.error("Error encountered reading mixin config %s: %s %s"
                    .formatted(configFile, ex.getClass().getName(), ex.getMessage()), ex);
        }

        registerConfiguration(config);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static void registerConfiguration(@Nullable Config config) {
        Field registeredConfigsField = Mixins.class.getDeclaredField("registeredConfigs");
        registeredConfigsField.setAccessible(true);
        Set<String> registeredConfigs = (Set<String>) registeredConfigsField.get(null);
        if (config == null || registeredConfigs.contains(config.getName())) {
            log.warn("Config %s is null or has already been registered!".formatted(config));
            return;
        }

        MixinEnvironment env = config.getEnvironment();
        if (env != null) {
            Method registerConfigMethod = MixinEnvironment.class.getDeclaredMethod("registerConfig", String.class);
            registerConfigMethod.setAccessible(true);
            registerConfigMethod.invoke(env, config.getName());
        }

        // Mixins.getConfigs().add(config); this would cause a ConcurrentModificationException
        // instead we select the config
        selectConfig(config);

        registeredConfigs.add(config.getName());

        Config parent = config.getParent();
        if (parent != null) {
            registerConfiguration(parent);
        }
    }

    // MixinProcessor.selectConfigs
    private static void selectConfig(Config handle) {
        try {
            Method get = Config.class.getDeclaredMethod("get");
            get.setAccessible(true);
            Object config = get.invoke(handle);

            Class<?> mixinConfig = Class.forName("org.spongepowered.asm.mixin.transformer.MixinConfig");
            Method select = mixinConfig.getDeclaredMethod("select", MixinEnvironment.class);
            select.setAccessible(true);

            if ((boolean) select.invoke(config, MixinEnvironment.getDefaultEnvironment()/* not current!!!!!!!*/)) {
                Method onSelect = mixinConfig.getDeclaredMethod("onSelect");
                onSelect.setAccessible(true);
                onSelect.invoke(config);
                getPendingConfigs().add(config);
            }
        } catch (Exception ex) {
            log.warn(String.format("Failed to select mixin config: %s", handle), ex);
        }

        // probably not necessary since this is already being called from selectConfigs which will sort right after?
        // Collections.sort(this.pendingConfigs);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static List<Object> getPendingConfigs() {
        IMixinTransformer transformer = getActualTransformer();
        Class<?> transformerImpl = Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer");

        Field processorField = transformerImpl.getDeclaredField("processor");
        processorField.setAccessible(true);

        Object mixinProcessor = processorField.get(transformer);

        Class<?> mixinProcessorClass = Class.forName("org.spongepowered.asm.mixin.transformer.MixinProcessor");
        Field pendingConfigsField = mixinProcessorClass.getDeclaredField("pendingConfigs");
        pendingConfigsField.setAccessible(true);

        return (List<Object>) pendingConfigsField.get(mixinProcessor);
    }

    @SneakyThrows
    public static IMixinTransformer getTransformer() {
        Object classLoaderInterface = getClassLoaderInterface();
        Field mixinTransformerField = getTransformerField();
        mixinTransformerField.setAccessible(true);
        return (IMixinTransformer) mixinTransformerField.get(classLoaderInterface);
    }

    @SneakyThrows
    public static void setTransformer(IMixinTransformer transformer) {
        Object classLoaderInterface = getClassLoaderInterface();
        Field mixinTransformerField = getTransformerField();
        mixinTransformerField.setAccessible(true);
        mixinTransformerField.set(classLoaderInterface, transformer);
    }

    @SneakyThrows
    public static Field getTransformerField() {
        Class<?> knotClassDelegate = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
        return knotClassDelegate.getDeclaredField("mixinTransformer");
    }

    @SneakyThrows
    public static Object getClassLoaderInterface() {
        Knot launcher = (Knot) FabricLauncherBase.getLauncher(); // lets just hope this is Knot for now
        Field classLoaderField = Knot.class.getDeclaredField("classLoader");
        classLoaderField.setAccessible(true);
        return classLoaderField.get(launcher);
    }

    @SneakyThrows
    public static IMixinTransformer getActualTransformer() {
        IMixinTransformer transformer = getTransformer();
        Class<?> transformerImpl = Class.forName("org.spongepowered.asm.mixin.transformer.MixinTransformer");
        while (!transformerImpl.isInstance(transformer)) {
            log.debug("Transformer is not a MixinTransformer implementation, searching for delegates...");
            Class<?> clazz = transformer.getClass();
            boolean foundDelegate = false;
            while (clazz != Object.class) {
                log.debug("Checking " + clazz.getName());
                try {
                    Field delegate = clazz.getDeclaredField("delegate");
                    delegate.setAccessible(true);
                    transformer = (IMixinTransformer) delegate.get(transformer);
                    foundDelegate = true;
                    break;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                } catch (ClassCastException e) {
                    log.error("Found delegate field on " + clazz.getName() + " but it was not a IMixinTransformer", e);
                    clazz = clazz.getSuperclass();
                }
            }

            if (!foundDelegate) {
                throw new NoSuchFieldException("Failed to find delegate field on " + transformer.getClass());
            }
        }

        return transformer;
    }

}
