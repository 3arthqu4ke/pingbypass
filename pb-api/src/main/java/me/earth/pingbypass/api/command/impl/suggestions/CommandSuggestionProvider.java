package me.earth.pingbypass.api.command.impl.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.command.impl.arguments.DescriptionArgumentType;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "of")
public class CommandSuggestionProvider<S> implements SuggestionProvider<S> {
    private final CommandManager commandManager;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return DescriptionArgumentType.suggest(builder, commandManager);
    }

}
