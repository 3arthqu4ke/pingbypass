package me.earth.pingbypass.server.commands;

import me.earth.pingbypass.api.command.GenericCommandManager;
import me.earth.pingbypass.api.command.commands.AbstractHelpCommand;
import me.earth.pingbypass.server.commands.api.ServerCommand;
import me.earth.pingbypass.server.commands.api.ServerCommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;

public class HelpCommand extends AbstractHelpCommand<ServerCommandSource> implements ServerCommand {
    public HelpCommand(GenericCommandManager<ServerCommandSource, ?> commandManager) {
        super(commandManager, "help", "Displays available commands.");
    }

    @Override
    protected void print(ServerCommandSource source, Component component) {
        source.getSession().send(new ClientboundSystemChatPacket(component, false));
    }

}
