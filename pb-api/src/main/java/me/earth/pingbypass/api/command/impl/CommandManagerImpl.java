package me.earth.pingbypass.api.command.impl;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import lombok.Getter;
import lombok.Setter;
import me.earth.pingbypass.api.command.Command;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.builder.ExtendedLiteralArgumentBuilder;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.synchronizedMap;

public class CommandManagerImpl extends SortedRegistry<Command> implements CommandManager {
    private final Map<Command, CommandNode<CommandSource>> builder2Node = synchronizedMap(new LinkedHashMap<>());
    private CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
    @Getter
    @Setter
    private String prefix = "+";

    @Override
    public boolean register(Command builder) {
        synchronized (lock) {
            if (super.register(builder)) {
                var command = ExtendedLiteralArgumentBuilder.<CommandSource>literal(builder.getName());
                builder.build(command);
                var node = command.build();
                builder2Node.put(builder, node);
                dispatcher.getRoot().addChild(node);
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean unregister(Command builder) {
        synchronized (lock) {
            // because CommandDispatcher does not support unregistering, we have to build a new one (or reflection...)
            //  (or could we register a delegating node and set its delegate to null on unregistering?)
            if (super.unregister(builder)) {
                builder2Node.remove(builder);
                CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
                builder2Node.values().forEach(node -> dispatcher.getRoot().addChild(node));
                this.dispatcher = dispatcher;
                return true;
            }

            return false;
        }
    }

    @Override
    public void execute(String command, CommandSource source) throws CommandSyntaxException {
        dispatcher.execute(command, source);
    }

    @Override
    public ParseResults<CommandSource> parse(StringReader reader, CommandSource source) {
        return dispatcher.parse(reader, source);
    }

    @Override
    public CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<CommandSource> parse,
                                                                   int cursor) {
        // TODO: is this needed? Better be safe, a command could get unregistered between parse and getSuggestions?
        if (!dispatcher.equals(parse.getContext().getDispatcher())) {
            return Suggestions.empty();
        }

        return dispatcher.getCompletionSuggestions(parse, cursor);
    }

    @Override
    public Map<CommandNode<CommandSource>, String> getSmartUsage(CommandNode<CommandSource> node,
                                                                 CommandSource source) {
        return dispatcher.getSmartUsage(node, source);
    }

}
