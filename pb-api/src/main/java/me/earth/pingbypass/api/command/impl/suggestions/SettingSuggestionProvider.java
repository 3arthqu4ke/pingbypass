package me.earth.pingbypass.api.command.impl.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.FindsArgument;
import me.earth.pingbypass.api.setting.Setting;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(staticName = "of")
public class SettingSuggestionProvider implements SuggestionProvider<CommandSource>, FindsArgument {
    private final String settingArgName;
    private final String argName;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> context,
                                                         SuggestionsBuilder builder) {
        Setting<?> setting = getArgument(context, settingArgName, Setting.class);
        if (setting == null) {
            return Suggestions.empty();
        }

        if (!hasArgument(context, argName)) {
            return SharedSuggestionProvider.suggest(setting.getArgumentType().getExamples(), builder);
        }

        return setting.getArgumentType().listSuggestions(context, builder);
    }

}
