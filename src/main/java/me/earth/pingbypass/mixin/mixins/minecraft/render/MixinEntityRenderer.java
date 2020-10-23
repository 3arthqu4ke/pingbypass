package me.earth.pingbypass.mixin.mixins.minecraft.render;

import me.earth.pingbypass.client.modules.servermodule.ServerModule;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer
{
    @Inject(method = "updateCameraAndRender", at = @At("HEAD"), cancellable = true)
    public void updateCameraAndRenderHook(float partialTicks, long nanoTime, CallbackInfo info)
    {
        if (ServerModule.getInstance().shouldNoRender())
        {
            info.cancel();
        }
    }

}
