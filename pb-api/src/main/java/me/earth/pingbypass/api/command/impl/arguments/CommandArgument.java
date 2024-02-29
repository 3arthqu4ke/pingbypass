package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.GenericCommand;
import me.earth.pingbypass.api.traits.Streamable;

@Getter
@RequiredArgsConstructor(staticName = "of")
public final class CommandArgument<S, C extends GenericCommand<S>> implements DescriptionArgumentType<C> {
    private final String type = "command";
    private final Streamable<C> nameables;

}
