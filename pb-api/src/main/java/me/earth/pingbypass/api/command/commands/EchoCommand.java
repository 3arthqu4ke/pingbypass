package me.earth.pingbypass.api.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import me.earth.pingbypass.api.command.CommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class EchoCommand extends AbstractPbCommand {
    public EchoCommand(PingBypass pingBypass, Minecraft minecraft) {
        super("echo", "Prints a message in chat.", pingBypass, minecraft);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(greedy("message").executes(ctx -> {
            String message = ctx.getArgument("message", String.class);
            print(Component.literal(message));
        }));
    }

}
