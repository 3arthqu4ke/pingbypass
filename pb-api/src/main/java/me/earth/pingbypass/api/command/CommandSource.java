package me.earth.pingbypass.api.command;

import me.earth.pingbypass.PingBypass;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;

public interface CommandSource extends SharedSuggestionProvider {
    PingBypass getPingBypass();

    Minecraft getMinecraft();

    default Chat getChat() {
        return getPingBypass().getChat();
    }

}
