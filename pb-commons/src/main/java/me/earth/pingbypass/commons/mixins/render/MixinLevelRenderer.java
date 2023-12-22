package me.earth.pingbypass.commons.mixins.render;

import me.earth.pingbypass.commons.event.render.ParticleEvent;
import me.earth.pingbypass.commons.util.mixin.MixinHelper;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Inject(
        method = "addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At("HEAD"),
        cancellable = true)
    private void addParticleInternalHook(ParticleOptions options, boolean force, boolean decreased, double x, double y,
                                         double z, double xSpeed, double ySpeed, double zSpeed,
                                         CallbackInfoReturnable<Particle> cir) {
        MixinHelper.hook(new ParticleEvent(options.getType()), cir);
    }

}
