package me.earth.pingbypass.api.launch;

import lombok.extern.slf4j.Slf4j;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@Slf4j
public class ApiMixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        log.info("Loading from MixinConnector: " + getClass().getName());
        Mixins.addConfiguration("mixins.pingbypass.json");
        this.onConnect();
        new SideLaunchingPlugin();
    }

    protected void onConnect() {
        // to be implemented by subclasses
    }

}
