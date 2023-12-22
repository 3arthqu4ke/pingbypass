package me.earth.pingbypass.commons.launch;

import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.config.JsonSerializable;
import me.earth.pingbypass.api.plugin.PluginConfig;
import me.earth.pingbypass.api.plugin.PluginConfigContainer;
import me.earth.pingbypass.api.plugin.PluginMixinConnector;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.commons.Constants;
import me.earth.pingbypass.commons.platform.PlatformProvider;
import me.earth.pingbypass.commons.util.ReflectionUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
final class PluginDiscoveryServiceImpl implements PluginDiscoveryService {
    private final List<PluginConfigContainer> pluginConfigs = new ArrayList<>();
    private final PlatformProvider platformProvider;
    private final Path[] directories;
    private final Side side;

    @Override
    public void load() {
        Arrays.stream(directories).forEach(this::checkDirectory);
        loadClasspathPluginConfigs();
    }

    @Override
    public Stream<PluginConfigContainer> getPluginConfigs() {
        return pluginConfigs.stream();
    }

    private void loadClasspathPluginConfigs() {
        String classPathPlugins = System.getProperty(Constants.CLASSPATH_PLUGINS);
        if (classPathPlugins != null) {
            for (String classpathConfig : classPathPlugins.split(";")) {
                log.info("Loading PluginConfig %s from classpath".formatted(classpathConfig));
                try (var is = getClass().getClassLoader().getResourceAsStream(classpathConfig)) {
                    if (is == null) {
                        log.error("Could not find classpath PluginConfig " + classpathConfig);
                        continue;
                    }

                    var isr = new InputStreamReader(is);
                    PluginConfig pluginConfig = JsonSerializable.GSON.fromJson(isr, PluginConfig.class);
                    log.info("%s found classpath PluginConfig: %s".formatted(side, pluginConfig));
                    initializePluginConfig(pluginConfig, null);
                } catch (IOException | JsonParseException | ReflectiveOperationException e) {
                    log.error("Failed to read classpath PluginConfig " + classpathConfig, e);
                }
            }
        }
    }

    private void checkDirectory(Path path) {
        try (var files = Files.walk(path)) {
            files.forEach(file -> {
                if (!Files.isDirectory(file) && file.toString().endsWith(".jar")) {
                    readJar(file);
                }
            });
        } catch (IOException e) {
            log.error("Failed to load plugins in " + path.toAbsolutePath(), e);
        }
    }

    private void readJar(Path path) {
        log.debug("Checking plugin jar " + path.getFileName());
        try (var jarFile = new JarFile(path.toFile())) {
            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            String configName = attributes.getValue(PluginConfig.MANIFEST_ENTRY);
            if (configName == null) {
                log.debug("Plugin %s does not specify a config in its Manifest, using pb.pluginconfig.json"
                        .formatted(path));
                configName = "pb.pluginconfig.json";
            }

            JarEntry configEntry = jarFile.getJarEntry(configName);
            try (var isr = new InputStreamReader(jarFile.getInputStream(configEntry))) {
                PluginConfig pluginConfig = JsonSerializable.GSON.fromJson(isr, PluginConfig.class);
                log.info("%s found PluginConfig %s: %s".formatted(side, path, pluginConfig));
                initializePluginConfig(pluginConfig, path);
            }
        } catch (Exception e) {
            log.error("Failed to load plugin " + path, e);
        }
    }

    private void initializePluginConfig(PluginConfig pluginConfig, @Nullable Path path)
            throws ReflectiveOperationException {
        if (Arrays.asList(pluginConfig.getSupports()).contains(side)
                && Arrays.asList(pluginConfig.getPlatforms()).contains(platformProvider.getCurrent())) {
            log.debug("Plugin is supported, adding it to classpath.");
            // TODO: make it easier for plugins to depend on each other?
            // TODO: plugin priority?
            if (path != null) {
                platformProvider.getPlatformService().addToClassPath(path);
            }

            pluginConfigs.add(new PluginConfigContainer(pluginConfig, path));

            if (pluginConfig.getMixinConfig() != null) {
                platformProvider.getPlatformService().addMixinConfig(pluginConfig.getMixinConfig());
            }

            if (pluginConfig.getMixinConnector() != null) {
                Class<?> connectorClass = platformProvider.getPlatformService().load(pluginConfig.getMixinConnector());
                Object connector = ReflectionUtil.instantiateNoArgs(connectorClass);
                ((PluginMixinConnector) connector).connect(side);
            }
        } else {
            log.info("Unsupported plugin: %s, expected side %s, platform %s".formatted(
                    pluginConfig.getName(), side, platformProvider.getCurrent()));
        }
    }

}
