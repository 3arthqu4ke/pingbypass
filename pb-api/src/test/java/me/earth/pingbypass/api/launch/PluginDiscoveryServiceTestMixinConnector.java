package me.earth.pingbypass.api.launch;

import me.earth.pingbypass.api.plugin.PluginMixinConnector;
import me.earth.pingbypass.api.side.Side;

public class PluginDiscoveryServiceTestMixinConnector implements PluginMixinConnector {
    public static PluginDiscoveryServiceTestMixinConnector instance;
    public static Side side;

    @Override
    public void connect(Side sideIn) {
        instance = this;
        side = sideIn;
    }

}
