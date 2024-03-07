package me.earth.pingbypass.api.command.commands;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.CommandNode;
import me.earth.pingbypass.api.command.GenericCommand;
import me.earth.pingbypass.api.command.GenericCommandManager;
import me.earth.pingbypass.api.command.impl.AbstractGenericCommand;
import me.earth.pingbypass.api.command.impl.arguments.CommandArgument;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.command.components.NameableComponent;
import net.minecraft.network.chat.Component;

import java.util.Map;

// TODO: do not list module commands?
public abstract class AbstractHelpCommand<S> extends AbstractGenericCommand<S> {
    private static final SimpleCommandExceptionType ERROR_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.help.failed"));
    private final GenericCommandManager<S, ?> commandManager;

    public AbstractHelpCommand(GenericCommandManager<S, ?> commandManager, String name, String description) {
        super(name, description);
        this.commandManager = commandManager;
    }

    protected abstract void print(S source, Component component);

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void build(LiteralArgumentBuilder<S> builder) {
        builder.executes(ctx -> {
            print(ctx.getSource(), NameableComponent.builder(commandManager)
                    .withName("Commands")
                    .suggestCommand(Nameable::getName)
                    .build());

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        }).then(arg("command", CommandArgument.of(commandManager)).executes(ctx -> {
            GenericCommand<?> command = ctx.getArgument("command", GenericCommand.class);
            ParseResults<S> results = commandManager.parse(command.getName(), ctx.getSource());

            if (results.getContext().getNodes().isEmpty()) {
                throw ERROR_FAILED.create();
            }

            Map<CommandNode<S>, String> map =
                    commandManager.getSmartUsage(((ParsedCommandNode) Iterables.getLast(results.getContext().getNodes())).getNode(), ctx.getSource());
            for (String string : map.values()) {
                print(ctx.getSource(), Component.literal(commandManager.getPrefix() + results.getReader().getString() + " " + string));
            }

            return map.size();
        }));
    }

}
