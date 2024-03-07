package me.earth.pingbypass.server.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.api.launch.SimpleMixinConfigPlugin;
import me.earth.pingbypass.api.plugin.PluginMixinConnector;
import me.earth.pingbypass.api.side.Side;

@Slf4j
public class ServerMixinConfigPlugin implements SimpleMixinConfigPlugin, PluginMixinConnector {
    public ServerMixinConfigPlugin() {
        log.info("Loading ServerMixinConfigPlugin");
        PreLaunchServiceImpl.INSTANCE.init(Side.SERVER);
    }

    @Override
    public void connect(Side side) {
        // NOP
    }

}
