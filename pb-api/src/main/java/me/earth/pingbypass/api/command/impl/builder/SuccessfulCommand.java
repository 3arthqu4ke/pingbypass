package me.earth.pingbypass.api.command.impl.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

/**
 * A {@link Command} which always returns {@link Command#SINGLE_SUCCESS} on {@link Command#run(CommandContext)}.
 *
 * @param <S> type of the source.
 */
@FunctionalInterface
public interface SuccessfulCommand<S> extends Command<S> {
    void runSuccessfully(CommandContext<S> context) throws CommandSyntaxException;

    @Override
    default int run(CommandContext<S> context) throws CommandSyntaxException {
        this.runSuccessfully(context);
        return SINGLE_SUCCESS;
    }

}
