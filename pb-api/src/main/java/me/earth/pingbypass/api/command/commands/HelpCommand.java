package me.earth.pingbypass.api.command.commands;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.Command;
import me.earth.pingbypass.api.command.CommandSource;
import net.minecraft.network.chat.Component;

public final class HelpCommand extends AbstractHelpCommand<CommandSource> implements Command {
    private final PingBypass pingBypass;

    public HelpCommand(PingBypass pingBypass) {
        super(pingBypass.getCommandManager(), "help", "Displays available commands.");
        this.pingBypass = pingBypass;
    }

    @Override
    protected void print(CommandSource source, Component component) {
        pingBypass.getChat().send(component);
    }

}
