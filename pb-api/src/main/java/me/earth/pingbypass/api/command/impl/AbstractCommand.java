package me.earth.pingbypass.api.command.impl;

import me.earth.pingbypass.api.command.Command;
import me.earth.pingbypass.api.command.CommandSource;

public abstract class AbstractCommand extends AbstractGenericCommand<CommandSource> implements Command {
    public AbstractCommand(String name, String description) {
        super(name, description);
    }

}
