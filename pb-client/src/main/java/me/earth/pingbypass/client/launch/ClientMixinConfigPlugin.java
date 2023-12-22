package me.earth.pingbypass.client.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.commons.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.commons.launch.SimpleMixinConfigPlugin;

@Slf4j
public class ClientMixinConfigPlugin extends SimpleMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        log.info("Loading ClientMixinConfigPlugin");
        PreLaunchServiceImpl.INSTANCE.init(Side.CLIENT);
    }

}
