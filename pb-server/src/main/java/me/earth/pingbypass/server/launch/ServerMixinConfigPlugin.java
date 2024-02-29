package me.earth.pingbypass.server.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.api.launch.SimpleMixinConfigPlugin;

@Slf4j
public class ServerMixinConfigPlugin implements SimpleMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        log.info("Loading ServerMixinConfigPlugin");
        PreLaunchServiceImpl.INSTANCE.init(Side.SERVER);
    }

}
