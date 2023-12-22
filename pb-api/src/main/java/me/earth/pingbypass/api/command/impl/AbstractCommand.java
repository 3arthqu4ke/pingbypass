package me.earth.pingbypass.api.command.impl;

import lombok.Data;
import me.earth.pingbypass.api.command.Command;

@Data
public abstract class AbstractCommand implements Command {
    private final String name;
    private final String description;

}
