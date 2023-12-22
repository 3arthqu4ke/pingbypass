package me.earth.pingbypass.api.plugin.impl;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.plugin.PluginContainer;
import me.earth.pingbypass.api.plugin.PluginManager;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;

@RequiredArgsConstructor
public class PluginManagerImpl extends SortedRegistry<PluginContainer> implements PluginManager {
    @Override
    protected boolean unregisterSynchronously(PluginContainer container) {
        if (super.unregisterSynchronously(container)) {
            container.getPlugin().unload();
            return true;
        }

        return false;
    }

}
