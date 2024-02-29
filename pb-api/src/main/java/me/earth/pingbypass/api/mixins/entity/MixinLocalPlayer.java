package me.earth.pingbypass.api.mixins.entity;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.loop.LocalPlayerUpdateEvent;
import me.earth.pingbypass.api.module.movement.Velocity;
import me.earth.pingbypass.api.util.mixin.MixinHelper;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer {
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
            shift = At.Shift.BEFORE))
    private void tickHook(CallbackInfo ci) {
        PingBypassApi.getEventBus().post(new LocalPlayerUpdateEvent(LocalPlayer.class.cast(this)));
    }

    @Inject(method = "moveTowardsClosestSpace", at = @At("HEAD"), cancellable = true)
    private void moveTowardsClosestSpaceHook(double d, double e, CallbackInfo ci) {
        MixinHelper.hook(new Velocity.PushOutOfBlocks(), ci);
    }

}
