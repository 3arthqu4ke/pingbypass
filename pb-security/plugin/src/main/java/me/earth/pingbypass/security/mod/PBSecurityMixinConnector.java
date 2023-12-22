package me.earth.pingbypass.security.mod;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

@SuppressWarnings("unused")
public class PBSecurityMixinConnector implements IMixinConnector {
    @Override
    public void connect() {
        Mixins.addConfiguration("mixins.pingbypass_security.json");
    }
}
