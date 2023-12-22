package me.earth.pingbypass.commons.launch;

import lombok.extern.slf4j.Slf4j;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@Slf4j
public abstract class AbstractCommonMixinConnector implements IMixinConnector {
    @Override
    public final void connect() {
        log.info("Loading from MixinConnector: " + getClass().getName());
        Mixins.addConfiguration("mixins.pingbypass_commons.json");
        this.onConnect();
    }

    protected abstract void onConnect();

}
