package me.earth.pingbypass.commons.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.earth.pingbypass.commons.module.render.NoRender;
import me.earth.pingbypass.commons.util.mixin.MixinHelper;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "displayItemActivation", at = @At("HEAD"), cancellable = true)
    private void displayItemActivationHook(ItemStack arg, CallbackInfo ci) {
        if (arg != null && arg.getItem() == Items.TOTEM_OF_UNDYING) {
            MixinHelper.hook(new NoRender.Totems(), ci);
        }
    }

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void bobHurtHook(PoseStack arg, float f, CallbackInfo ci) {
        MixinHelper.hook(new NoRender.Hurt(), ci);
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobViewHook(PoseStack arg, float f, CallbackInfo ci) {
        MixinHelper.hook(new NoRender.Bob(), ci);
    }

}
