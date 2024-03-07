package me.earth.pingbypass.api.gui;

import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@UtilityClass
public class ImageButtonUtil {
    private static final ResourceLocation ICON_BUTTON = new ResourceLocation("pingbypass:icon/icon_button");
    private static final ResourceLocation ICON_BUTTON_FOCUSED = new ResourceLocation("pingbypass:icon/icon_button_focused");

    public static ImageButton getIconButton(int x, int y, Button.OnPress callback) {
        return new ImageButton(x, y, 20, 20, new WidgetSprites(ICON_BUTTON, ICON_BUTTON_FOCUSED), callback, Component.literal("PingBypassCommandsScreen"));
    }

}
