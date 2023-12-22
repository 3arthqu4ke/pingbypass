package me.earth.pingbypass.api.plugin.impl;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.plugin.Plugin;

public abstract class AbstractUnloadablePlugin implements Plugin {
    private PluginUnloadingService unloadingService;

    public abstract void load(PingBypass pingBypass, PluginUnloadingService service);

    @Override
    public void load(PingBypass pingBypass) {
        var unloadingService = new PluginUnloadingService(pingBypass);
        this.unloadingService = unloadingService;
        this.load(pingBypass, unloadingService);
    }

    @Override
    public void unload() {
        PluginUnloadingService pluginUnloadingService = unloadingService;
        if (pluginUnloadingService != null) {
            pluginUnloadingService.unload();
        }
    }

    @Override
    public boolean supportsUnloading() {
        return true;
    }

}
