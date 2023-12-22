package me.earth.pingbypass.commons.mixins.render;

import me.earth.pingbypass.commons.module.render.NoRender;
import me.earth.pingbypass.commons.util.mixin.MixinHelper;
import net.minecraft.world.level.lighting.SkyLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyLightEngine.class)
public abstract class MixinSkylightEngine {
    @Inject(method = "propagateIncrease", at = @At("HEAD"), cancellable = true)
    private void checkNeighborsAfterUpdateHook(long l, long m, int i, CallbackInfo ci) {
        MixinHelper.hook(new NoRender.Skylight(), ci);
    }

}
