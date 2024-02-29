package me.earth.pingbypass.api.command.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.FindsArgument;
import me.earth.pingbypass.api.config.Config;
import me.earth.pingbypass.api.config.ConfigManager;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "of")
public class ConfigSuggestionProvider implements SuggestionProvider<CommandSource>, FindsArgument {
    private final String configArgName;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> context,
                                                         SuggestionsBuilder builder) {
        Config<?> config = getArgument(context, configArgName, Config.class);
        if (config == null || config.stream().findAny().isEmpty()) {
            return SharedSuggestionProvider.suggest(new String[]{"<name>"}, builder);
        }

        return config instanceof ConfigManager cm
                ? SharedSuggestionProvider.suggest(cm.stream().flatMap(Config::stream).map(Nameable::getName), builder)
                : SharedSuggestionProvider.suggest(config.stream().map(Nameable::getName), builder);
    }

}
