package me.earth.pingbypass.commons.gui;

import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@UtilityClass
public class ImageButtonUtil {
    private static final ResourceLocation ICON_BUTTON = new ResourceLocation("pingbypass", "textures/gui/icon_button.png");

    public static ImageButton getIconButton(int x, int y, Button.OnPress callback) {
        return new ImageButton(x, y, 20, 20, 0, 0, 20, ICON_BUTTON, 32, 64, callback,
                Component.literal("PingBypassCommandsScreen"));
    }

}
