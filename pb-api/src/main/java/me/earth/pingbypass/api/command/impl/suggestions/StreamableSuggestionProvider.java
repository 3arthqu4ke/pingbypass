package me.earth.pingbypass.api.command.impl.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.impl.FindsArgument;
import me.earth.pingbypass.api.command.impl.arguments.DescriptionArgumentType;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;

import java.util.concurrent.CompletableFuture;

@Getter
@RequiredArgsConstructor
public class StreamableSuggestionProvider<T extends Nameable & HasDescription, S> implements SuggestionProvider<S>, FindsArgument {
    private final String streamableArg;
    private final Class<? extends Streamable<T>> streamableType;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Streamable<T> streamable = getArgument(context, streamableArg, streamableType);
        if (streamable == null) {
            return Suggestions.empty();
        }

        return DescriptionArgumentType.suggest(builder, streamable);
    }

}
