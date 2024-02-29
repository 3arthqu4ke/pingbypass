package me.earth.pingbypass.server.mixins;

import me.earth.pingbypass.api.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Component arg) {
        super(arg);
    }

    @Inject(method = "render", at = @At(value = "RETURN"))
    private void renderHook(GuiGraphics graphics, int x, int y, float delta, CallbackInfo ci) {
        graphics.drawString(this.font, Constants.NAME_VERSION, 2, 2, 0xffffffff);
    }

}
