package me.earth.pingbypass.api.plugin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.launch.PluginDiscoveryService;
import me.earth.pingbypass.api.platform.PlatformService;
import me.earth.pingbypass.api.util.ReflectionUtil;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

// TODO: add plugins at runtime!
// TODO: method to generally make it easier to register plugins afterwards, like in Phobot MixinInitializationService!
@Slf4j
@RequiredArgsConstructor
public class PluginInitializer {
    private final PluginDiscoveryService pluginDiscoveryService;
    private final PlatformService platformService;
    private final PingBypass pingBypass;

    @SuppressWarnings("unchecked")
    public void init() {
        pluginDiscoveryService.getPluginConfigs().forEach(container -> {
            PluginConfig config = container.getConfig();
            if (config.getMainClass() != null) {
                log.info("Instantiating Plugin %s : %s".formatted(config.getName(), config.getMainClass()));
                try {
                    Class<?> mainClass = platformService.load(config.getMainClass());
                    registerPlugin(config, container.getPath(), (Class<? extends Plugin>) mainClass);
                } catch (ReflectiveOperationException e) {
                    log.error("Failed to load plugin %s %s".formatted(config.getName(), config.getMainClass()), e);
                }
            }
        });
    }

    public void registerPlugin(PluginConfig config, @Nullable Path path, Class<? extends Plugin> pluginClass) throws ReflectiveOperationException {
        if (pingBypass.getPluginManager().stream().map(PluginContainer::getPlugin).noneMatch(p -> p.getClass() == pluginClass)) {
            Plugin plugin = ReflectionUtil.instantiateNoArgs(pluginClass);
            pingBypass.getPluginManager().register(new PluginContainer(plugin, path, config));
            plugin.load(pingBypass);
        } else {
            log.warn("A Plugin of type " + pluginClass.getName() + " has already been installed.");
        }
    }

}
