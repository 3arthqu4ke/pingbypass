package me.earth.pingbypass.api.util;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.command.Chat;
import me.earth.pingbypass.api.module.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@UtilityClass
public class ModuleUtil {
    public static void sendModuleToggleMessage(Chat chat, Module module, boolean sendBeforeActuallyToggled) {
        boolean willBeEnabled = sendBeforeActuallyToggled != module.isEnabled();
        String message = willBeEnabled ? " enabled." : " disabled.";
        ChatFormatting formatting = willBeEnabled ? ChatFormatting.GREEN : ChatFormatting.RED;
        chat.sendWithoutLogging(Component.literal("")
                .append(Component.literal(module.getName()).withStyle(ChatFormatting.BOLD))
                .append(Component.literal(message).withStyle(formatting)), module.getName());
    }

}
