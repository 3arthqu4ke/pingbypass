package me.earth.pingbypass.server.commands.api;

import me.earth.pingbypass.api.command.impl.AbstractGenericCommand;
import me.earth.pingbypass.server.PingBypassServer;

public abstract class AbstractServerCommand extends AbstractGenericCommand<ServerCommandSource> implements ServerCommand {
    protected final PingBypassServer server;

    public AbstractServerCommand(PingBypassServer server, String name, String description) {
        super(name, description);
        this.server = server;
    }

}
