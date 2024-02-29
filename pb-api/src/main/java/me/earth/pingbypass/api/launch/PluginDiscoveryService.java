package me.earth.pingbypass.api.launch;

import me.earth.pingbypass.api.plugin.PluginConfigContainer;

import java.util.stream.Stream;

public interface PluginDiscoveryService {
    void load();

    Stream<PluginConfigContainer> getPluginConfigs();

}
