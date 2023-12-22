package me.earth.pingbypass.commons.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.Chat;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.AbstractCommand;
import me.earth.pingbypass.api.command.impl.arguments.StringArgument;
import me.earth.pingbypass.api.command.impl.builder.ExtendedLiteralArgumentBuilder;
import me.earth.pingbypass.api.command.impl.builder.ExtendedRequiredArgumentBuilder;
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

    protected <T> ExtendedRequiredArgumentBuilder<CommandSource, T> arg(String name, ArgumentType<T> type) {
        return new ExtendedRequiredArgumentBuilder<>(type, name);
    }

    protected ExtendedLiteralArgumentBuilder<CommandSource> literal(String literal) {
        return new ExtendedLiteralArgumentBuilder<>(literal);
    }

    protected ExtendedRequiredArgumentBuilder<CommandSource, String> greedy(String name) {
        return new ExtendedRequiredArgumentBuilder<>(StringArgument.greedy(name), name);
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
