package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class DelegatingArgumentType<T> implements ArgumentType<T> {
    @Delegate
    private final ArgumentType<T> delegate;

}
