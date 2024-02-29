package me.earth.pingbypass.api.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.components.NameableComponent;

public class PluginsCommand extends AbstractPbCommand {
    public PluginsCommand(PingBypass pingBypass) {
        super("plugins", "Lists plugins currently installed.", pingBypass);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            print(NameableComponent
                    .builder(pingBypass.getPluginManager())
                    .withName("Plugins")
                    .build());

            return Command.SINGLE_SUCCESS;
        });
    }

}
