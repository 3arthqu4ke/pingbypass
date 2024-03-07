package me.earth.pingbypass.client.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.api.launch.SimpleMixinConfigPlugin;
import me.earth.pingbypass.api.plugin.PluginMixinConnector;
import me.earth.pingbypass.api.side.Side;

@Slf4j
public class ClientMixinConfigPlugin implements SimpleMixinConfigPlugin, PluginMixinConnector {
    public ClientMixinConfigPlugin() {
        log.info("Loading ClientMixinConfigPlugin");
        PreLaunchServiceImpl.INSTANCE.init(Side.CLIENT);
    }

    @Override
    public void connect(Side side) {
        // NOP
    }

}
