package me.earth.pingbypass.server.launch;

import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.launch.ApiMixinConnector;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import org.spongepowered.asm.mixin.Mixins;

@SuppressWarnings("unused")
public class ServerMixinConnector extends ApiMixinConnector {
    @Override
    protected void onConnect() {
        Mixins.addConfiguration("mixins.pingbypass_server.json");
        PreLaunchServiceImpl.INSTANCE.init(Side.SERVER);
    }

}
