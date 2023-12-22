package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.network.chat.Component;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Argument type which suggests the names from a {@link Iterable} of {@link Nameable}s and displays a tooltip with that
 * nameables {@link HasDescription#getDescription()}.
 *
 * @param <T> the type of nameables this ArgumentType uses.
 */
public interface DescriptionArgumentType<T extends Nameable & HasDescription> extends NameableArgumentType<T> {
    @Override
    default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return suggest(builder, getNameables());
    }

    static <N extends Nameable & HasDescription> CompletableFuture<Suggestions> suggest(SuggestionsBuilder builder,
                                                                                        Iterable<N> nameables) {
        String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
        for (N nameable : nameables) {
            if (matchesSubStr(remaining, nameable.getNameLowerCase())) {
                builder.suggest(nameable.getName(), Component.literal(nameable.getDescription()));
            }
        }

        return builder.buildFuture();
    }

    /**
     * @see CommandSource#matchesSubStr(String, String)
     */
    private static boolean matchesSubStr(String subString, String string) {
        for(int i = 0; !string.startsWith(subString, i); ++i) {
            i = string.indexOf('_', i);
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

}
