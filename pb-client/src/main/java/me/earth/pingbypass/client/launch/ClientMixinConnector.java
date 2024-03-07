package me.earth.pingbypass.client.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.launch.ApiMixinConnector;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.api.side.Side;
import org.spongepowered.asm.mixin.Mixins;

@Slf4j
@SuppressWarnings("unused")
public class ClientMixinConnector extends ApiMixinConnector {
    @Override
    protected void onConnect() {
        Mixins.addConfiguration("mixins.pingbypass_client.json");
        PreLaunchServiceImpl.INSTANCE.init(Side.CLIENT);
    }

}
