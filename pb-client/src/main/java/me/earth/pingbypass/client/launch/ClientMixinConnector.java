package me.earth.pingbypass.client.launch;

import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.launch.ApiMixinConnector;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import org.spongepowered.asm.mixin.Mixins;

@SuppressWarnings("unused")
public class ClientMixinConnector extends ApiMixinConnector {
    @Override
    protected void onConnect() {
        Mixins.addConfiguration("mixins.pingbypass_client.json");
        PreLaunchServiceImpl.INSTANCE.init(Side.CLIENT);
    }

}
