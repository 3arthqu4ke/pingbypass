package me.earth.pingbypass.commons.command.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.Command;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.arguments.CommandArgument;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.commons.command.AbstractPbCommand;
import me.earth.pingbypass.commons.command.components.NameableComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Map;

// TODO: do not list module commands?
public final class HelpCommand extends AbstractPbCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.help.failed"));

    public HelpCommand(PingBypass pingBypass, Minecraft minecraft) {
        super("help", "Helps with command usage.", pingBypass, minecraft);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // ParsedCommandNode
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            print(NameableComponent.builder(pingBypass.getCommandManager())
                                                         .withName("Commands")
                                                         .suggestCommand(Nameable::getName)
                                                         .build());
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        }).then(arg("command", CommandArgument.of(pingBypass.getCommandManager())).executes(ctx -> {
            Command command = ctx.getArgument("command", Command.class);
            ParseResults<CommandSource> results =
                    pingBypass.getCommandManager().parse(command.getName(), ctx.getSource());

            if (results.getContext().getNodes().isEmpty()) {
                throw ERROR_FAILED.create();
            }

            Map<CommandNode<CommandSource>, String> map =
                    pingBypass.getCommandManager().getSmartUsage(
                            ((ParsedCommandNode) Iterables.getLast(results.getContext().getNodes())).getNode(),
                            ctx.getSource());

            for (String string : map.values()) {
                print(Component.literal(
                        pingBypass.getCommandManager().getPrefix() + results.getReader().getString() + " " + string));
            }

            return map.size();
        }));
    }

}
