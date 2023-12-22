package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.builder.ExtendedRequiredArgumentBuilder;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;
import me.earth.pingbypass.api.traits.StreamableImpl;

@Getter
public class NameableArgumentTypeImpl<T extends Nameable> implements NameableArgumentType<T> {
    private final Streamable<T> nameables;
    private final String type;

    public NameableArgumentTypeImpl(Streamable<T> nameables, String type) {
        this.nameables = nameables;
        this.type = type;
    }

    @SafeVarargs
    public static <V extends Nameable> NameableArgumentType<V> of(String name, V... args) {
        return new NameableArgumentTypeImpl<>(StreamableImpl.of(args), name);
    }

    @SafeVarargs
    public static <V extends Nameable> ExtendedRequiredArgumentBuilder<CommandSource, V> builder(
            String name, V... args) {
        return new ExtendedRequiredArgumentBuilder<>(of(name, args), name);
    }

}
