package me.earth.pingbypass.api.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.Chat;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.AbstractCommand;
import me.earth.pingbypass.api.command.impl.builder.SuccessfulCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class AbstractPbCommand extends AbstractCommand {
    // TODO: this is not really needed anymore, use CommandSource everywhere!
    protected final PingBypass pingBypass;
    protected final Minecraft mc;

    public AbstractPbCommand(String name, String description, PingBypass pingBypass, Minecraft mc) {
        super(name, description);
        this.pingBypass = pingBypass;
        this.mc = mc;
    }

    public AbstractPbCommand(String name, String description, PingBypass pingBypass) {
        this(name, description, pingBypass, pingBypass.getMinecraft());
    }

    @SafeVarargs
    protected final <T extends ArgumentBuilder<CommandSource, T>> void executesWithOptionalArguments(
            SuccessfulCommand<CommandSource> command,
            T builder,
            ArgumentBuilder<CommandSource, ?>... arguments) {
        builder.executes(command);
        for (var argument : arguments) {
            builder.then(argument.executes(command));
        }
    }

    protected Chat getChat() {
        return pingBypass.getChat();
    }

    protected void print(Component message) {
        getChat().send(message);
    }

    protected void error(String message) {
        getChat().send(Component.literal(message).withStyle(ChatFormatting.RED));
    }

    protected void print(Component message, String identifier) {
        getChat().send(message, identifier);
    }

}
