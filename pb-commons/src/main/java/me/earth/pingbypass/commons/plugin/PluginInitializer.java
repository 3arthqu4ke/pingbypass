package me.earth.pingbypass.commons.plugin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.plugin.Plugin;
import me.earth.pingbypass.api.plugin.PluginConfig;
import me.earth.pingbypass.api.plugin.PluginContainer;
import me.earth.pingbypass.commons.launch.PluginDiscoveryService;
import me.earth.pingbypass.commons.platform.PlatformService;
import me.earth.pingbypass.commons.util.ReflectionUtil;

@Slf4j
@RequiredArgsConstructor
public class PluginInitializer {
    private final PluginDiscoveryService pluginDiscoveryService;
    private final PlatformService platformService;
    private final PingBypass pingBypass;

    public void init() {
        pluginDiscoveryService.getPluginConfigs().forEach(container -> {
            PluginConfig config = container.getConfig();
            if (config.getMainClass() != null) {
                log.info("Instantiating Plugin %s : %s".formatted(config.getName(), config.getMainClass()));
                try {
                    Class<?> mainClass = platformService.load(config.getMainClass());
                    Plugin plugin = (Plugin) ReflectionUtil.instantiateNoArgs(mainClass);
                    pingBypass.getPluginManager().register(new PluginContainer(plugin, container.getPath(), config));
                    plugin.load(pingBypass);
                } catch (ReflectiveOperationException e) {
                    log.error("Failed to load plugin %s %s".formatted(config.getName(), config.getMainClass()), e);
                }
            }
        });
    }

}
