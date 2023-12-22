package me.earth.pingbypass.server.mixins.headlessmc;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.command.CommandContextImpl;
import me.earth.pingbypass.server.headlessmc.HmcPbCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(value = CommandContextImpl.class, remap = false)
public abstract class MixinCommandContextImpl {
    @Shadow
    protected abstract void add(Command command);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initHook(HeadlessMc log, CallbackInfo ci) {
        this.add(new HmcPbCommand(log));
    }

}
