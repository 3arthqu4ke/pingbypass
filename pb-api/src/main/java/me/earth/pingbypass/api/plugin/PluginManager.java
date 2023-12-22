package me.earth.pingbypass.api.plugin;

import me.earth.pingbypass.api.registry.Registry;

public interface PluginManager extends Registry<PluginContainer> {
    // just a Registry<PluginContainer>, I do not want to take generics with me everywhere
}
