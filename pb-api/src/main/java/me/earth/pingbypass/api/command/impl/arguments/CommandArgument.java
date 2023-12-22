package me.earth.pingbypass.api.command.impl.arguments;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.Command;
import me.earth.pingbypass.api.traits.Streamable;

@Getter
@RequiredArgsConstructor(staticName = "of")
public final class CommandArgument implements DescriptionArgumentType<Command> {
    private final String type = "command";
    private final Streamable<Command> nameables;

}
