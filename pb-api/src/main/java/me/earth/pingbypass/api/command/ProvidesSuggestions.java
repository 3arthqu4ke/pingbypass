package me.earth.pingbypass.api.command;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ProvidesSuggestions<S> {
    ParseResults<S> parse(StringReader reader, S source);

    CompletableFuture<Suggestions> getCompletionSuggestions(ParseResults<S> parse, int cursor);

    Map<CommandNode<S>, String> getSmartUsage(CommandNode<S> node, S source);

    default ParseResults<S> parse(String command, S source) {
        return this.parse(new StringReader(command), source);
    }

}
