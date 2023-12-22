package me.earth.pingbypass.api.command.impl;

import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.arguments.StringArgument;
import me.earth.pingbypass.api.command.impl.builder.ExtendedLiteralArgumentBuilder;
import me.earth.pingbypass.api.command.impl.builder.ExtendedRequiredArgumentBuilder;

public interface UsesExtendedBuilders {
    default <T> ExtendedRequiredArgumentBuilder<CommandSource, T> arg(String name, ArgumentType<T> type) {
        return new ExtendedRequiredArgumentBuilder<>(type, name);
    }

    default ExtendedLiteralArgumentBuilder<CommandSource> literal(String literal) {
        return new ExtendedLiteralArgumentBuilder<>(literal);
    }

    default ExtendedRequiredArgumentBuilder<CommandSource, String> greedy(String name) {
        return new ExtendedRequiredArgumentBuilder<>(StringArgument.greedy(name), name);
    }

}
