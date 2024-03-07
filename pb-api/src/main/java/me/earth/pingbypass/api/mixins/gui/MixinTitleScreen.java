package me.earth.pingbypass.api.mixins.gui;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.gui.GuiScreenEvent;
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

    @Inject(method = "init", at = @At("RETURN"))
    private void initHook(CallbackInfo ci) {
        PingBypassApi.getEventBus().post(new GuiScreenEvent.TitleScreen<>(this), this.getClass());
    }

}
