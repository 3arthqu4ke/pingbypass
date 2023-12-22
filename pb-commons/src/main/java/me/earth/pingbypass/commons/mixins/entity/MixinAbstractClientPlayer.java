package me.earth.pingbypass.commons.mixins.entity;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.commons.module.render.NoRender;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer {
    @Inject(method = "getFieldOfViewModifier", at = @At("HEAD"), cancellable = true)
    private void getFieldOfViewModifierHook(CallbackInfoReturnable<Float> cir) {
        NoRender.Fov event = new NoRender.Fov();
        PingBypassApi.getEventBus().post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(1.0f);
        }
    }

}
