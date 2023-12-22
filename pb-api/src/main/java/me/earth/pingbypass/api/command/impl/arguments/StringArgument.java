package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * A custom {@link com.mojang.brigadier.arguments.StringArgumentType} but with a suggestion.
 * @see <a href="https://bugs.mojang.com/browse/MC-165562">MC-165562</a>
 */
public final class StringArgument extends DelegatingArgumentType<String> implements ArgumentType<String> {
    @Getter
    private final Collection<String> examples;
    private final String suggestion;

    private StringArgument(ArgumentType<String> delegate, String name) {
        super(delegate);
        this.examples = Collections.singleton(name);
        this.suggestion = "<%s>".formatted(name);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (builder.getRemaining().isEmpty()) {
            return builder.suggest(suggestion).buildFuture();
        }

        return Suggestions.empty();
    }

    public static StringArgument word(String argName) {
        return new StringArgument(StringArgumentType.word(), argName);
    }

    public static StringArgument string(String argName) {
        return new StringArgument(StringArgumentType.string(), argName);
    }

    public static StringArgument greedy(String argName) {
        return new StringArgument(StringArgumentType.greedyString(), argName);
    }

}
