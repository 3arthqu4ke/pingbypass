package me.earth.pingbypass.server.launch;

import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.commons.launch.AbstractCommonMixinConnector;
import me.earth.pingbypass.commons.launch.PreLaunchServiceImpl;
import org.spongepowered.asm.mixin.Mixins;

@SuppressWarnings("unused")
public class ServerMixinConnector extends AbstractCommonMixinConnector {
    @Override
    protected void onConnect() {
        Mixins.addConfiguration("mixins.pingbypass_server.json");
        PreLaunchServiceImpl.INSTANCE.init(Side.SERVER);
    }

}
