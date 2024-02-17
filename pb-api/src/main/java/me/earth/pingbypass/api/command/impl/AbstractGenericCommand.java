package me.earth.pingbypass.api.command.impl;

import com.mojang.brigadier.arguments.ArgumentType;
import lombok.Data;
import me.earth.pingbypass.api.command.GenericCommand;
import me.earth.pingbypass.api.command.impl.arguments.StringArgument;
import me.earth.pingbypass.api.command.impl.builder.ExtendedLiteralArgumentBuilder;
import me.earth.pingbypass.api.command.impl.builder.ExtendedRequiredArgumentBuilder;

@Data
public abstract class AbstractGenericCommand<S> implements GenericCommand<S> {
    private final String name;
    private final String description;

    public <T> ExtendedRequiredArgumentBuilder<S, T> arg(String name, ArgumentType<T> type) {
        return new ExtendedRequiredArgumentBuilder<>(type, name);
    }

    public ExtendedLiteralArgumentBuilder<S> literal(String literal) {
        return new ExtendedLiteralArgumentBuilder<>(literal);
    }

    public ExtendedRequiredArgumentBuilder<S, String> greedy(String name) {
        return new ExtendedRequiredArgumentBuilder<>(StringArgument.greedy(name), name);
    }

}
