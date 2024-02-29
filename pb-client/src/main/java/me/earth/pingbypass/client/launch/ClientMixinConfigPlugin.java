package me.earth.pingbypass.client.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.plugin.PluginMixinConnector;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.api.launch.SimpleMixinConfigPlugin;

@Slf4j
public class ClientMixinConfigPlugin implements SimpleMixinConfigPlugin, PluginMixinConnector {
    @Override
    public void onLoad(String mixinPackage) {
        log.info("Loading ClientMixinConfigPlugin");
        PreLaunchServiceImpl.INSTANCE.init(Side.CLIENT);
    }

    @Override
    public void connect(Side side) {

    }

}
