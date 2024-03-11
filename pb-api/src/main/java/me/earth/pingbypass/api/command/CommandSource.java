package me.earth.pingbypass.api.command;

import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.impl.arguments.StringArgument;
import me.earth.pingbypass.api.command.impl.builder.ExtendedLiteralArgumentBuilder;
import me.earth.pingbypass.api.command.impl.builder.ExtendedRequiredArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;

public interface CommandSource extends SharedSuggestionProvider {
    PingBypass getPingBypass();

    Minecraft getMinecraft();

    default Chat getChat() {
        return getPingBypass().getChat();
    }

    static <T> ExtendedRequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type) {
        return new ExtendedRequiredArgumentBuilder<>(type, name);
    }

    static ExtendedLiteralArgumentBuilder<CommandSource> literal(String literal) {
        return new ExtendedLiteralArgumentBuilder<>(literal);
    }

    static ExtendedRequiredArgumentBuilder<CommandSource, String> greedy(String name) {
        return new ExtendedRequiredArgumentBuilder<>(StringArgument.greedy(name), name);
    }

}
