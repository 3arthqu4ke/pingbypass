package me.earth.pingbypass.commons.launch;

import me.earth.pingbypass.api.plugin.PluginConfigContainer;

import java.util.stream.Stream;

public interface PluginDiscoveryService {
    void load();

    Stream<PluginConfigContainer> getPluginConfigs();

}
