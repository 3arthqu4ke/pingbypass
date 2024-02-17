package me.earth.pingbypass.server.commands;

import me.earth.pingbypass.api.command.GenericCommandManager;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.commands.api.ServerCommand;
import me.earth.pingbypass.server.commands.api.ServerCommandSource;

public class ServerCommandInitializer {
    public void init(PingBypassServer server) {
        GenericCommandManager<ServerCommandSource, ServerCommand> serverCommandManager =  server.getServerCommandManager();
        serverCommandManager.register(new HelpCommand(serverCommandManager));
        serverCommandManager.register(new ConnectCommand(server));
    }

}
