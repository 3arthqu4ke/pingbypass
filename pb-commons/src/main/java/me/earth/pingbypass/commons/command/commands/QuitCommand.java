package me.earth.pingbypass.commons.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.commons.command.AbstractPbCommand;
import net.minecraft.client.Minecraft;

public class QuitCommand extends AbstractPbCommand {
    public QuitCommand(PingBypass pingBypass, Minecraft mc) {
        super("quit", "Quits the game.", pingBypass, mc);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            mc.stop();
            return Command.SINGLE_SUCCESS;
        });
    }

}
