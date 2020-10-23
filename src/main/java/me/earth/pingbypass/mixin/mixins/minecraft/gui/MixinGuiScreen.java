package me.earth.pingbypass.mixin.mixins.minecraft.gui;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.text.TextColor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.server.PhobosServer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public class MixinGuiScreen implements Globals
{
    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void drawScreenHook(int mouseX, int mouseY, float partialTicks, CallbackInfo info)
    {
        PhobosServer server = PingBypass.server;
        if (server != null)
        {
            String text = "PhobosServer-" + PingBypass.VERSION + " Port: " + server.getPort() + (server.isConnected() ? TextColor.GREEN + " Connected" : "");
            mc.fontRenderer.drawStringWithShadow(text, 2, 2, 0xffffffff);
        }
    }

}
