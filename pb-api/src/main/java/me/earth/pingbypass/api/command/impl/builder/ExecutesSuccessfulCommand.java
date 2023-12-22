package me.earth.pingbypass.api.command.impl.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;

public interface ExecutesSuccessfulCommand<S, T extends ArgumentBuilder<S, T>> {
    T executes(Command<S> command);

    default T executes(SuccessfulCommand<S> command) {
        return executes((Command<S>) command);
    }

}
