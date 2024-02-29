package me.earth.pingbypass.api.mixins.render;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.api.module.render.NoRender;
import net.minecraft.client.Camera;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class MixinCamera {
    @Inject(method = "getFluidInCamera", at = @At("HEAD"), cancellable = true)
    private void getFluidInCameraHook(CallbackInfoReturnable<FogType> cir) {
        CancellableEvent event = new NoRender.Liquids();
        PingBypassApi.getEventBus().post(event);
        if (event.isCancelled()) {
            cir.setReturnValue(FogType.NONE);
        }
    }

}
