package me.earth.pingbypass.api.command.impl;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface FindsArgument {
    default boolean hasArgument(CommandContext<?> ctx, String argumentName) {
        return getArgumentNodes(ctx).anyMatch(node -> argumentName.equals(node.getName()));
    }

    default @Nullable <V> V getArgument(CommandContext<?> ctx, String argumentName, Class<V> type) {
        return getArgument(ctx, argumentName, type, null);
    }

    default <V> V getArgument(CommandContext<?> ctx, String argumentName, Class<V> type, V def) {
        if (hasArgument(ctx, argumentName)) {
            return ctx.getArgument(argumentName, type);
        }

        return def;
    }

    default Stream<ArgumentCommandNode<?,?>> getArgumentNodes(CommandContext<?> context) {
        return context.getNodes()
                .stream()
                .map(ParsedCommandNode::getNode)
                .filter(node -> node instanceof ArgumentCommandNode<?,?>)
                .map(node -> (ArgumentCommandNode<?,?>) node);
    }

}
