package me.earth.pingbypass.commons.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.earth.pingbypass.commons.module.render.NoRender;
import me.earth.pingbypass.commons.util.mixin.MixinHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class MixinScreenEffectRenderer {
    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void renderFireHook(Minecraft arg, PoseStack arg2, CallbackInfo ci) {
        MixinHelper.hook(new NoRender.Fire(), ci);
    }

    @Inject(method = "renderWater", at = @At("HEAD"), cancellable = true)
    private static void renderTexHook(Minecraft arg, PoseStack arg2, CallbackInfo ci) {
        MixinHelper.hook(new NoRender.Liquids(), ci);
    }

    @Inject(method = "renderTex", at = @At("HEAD"), cancellable = true)
    private static void renderTexHook(TextureAtlasSprite arg, PoseStack arg2, CallbackInfo ci) {
        MixinHelper.hook(new NoRender.Blocks(), ci);
    }

}
