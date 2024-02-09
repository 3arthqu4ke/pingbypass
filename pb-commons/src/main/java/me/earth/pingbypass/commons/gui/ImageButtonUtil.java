package me.earth.pingbypass.commons.gui;

import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@UtilityClass
public class ImageButtonUtil {
    private static final ResourceLocation ICON_BUTTON = new ResourceLocation("icon/icon_button.png");
    private static final ResourceLocation ICON_BUTTON_FOCUSED = new ResourceLocation("icon/icon_button_focused.png");

    public static ImageButton getIconButton(int x, int y, Button.OnPress callback) {
        return new ImageButton(x, y, 20, 20, new WidgetSprites(ICON_BUTTON, ICON_BUTTON_FOCUSED), callback, Component.literal("PingBypassCommandsScreen"));
    }

}
