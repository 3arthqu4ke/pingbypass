package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public interface NameableArgumentType<T extends Nameable> extends ArgumentType<T> {
    CommandExceptionType COULD_NOT_FIND_NAMEABLE = new CommandExceptionType() {};
    int EXAMPLE_SIZE = 3;

    String getType();

    Streamable<T> getNameables();

    @Override
    default T parse(StringReader reader) throws CommandSyntaxException {
        String arg = reader.readString();
        Optional<T> result = getNameables()
                .stream()
                .filter(n -> arg.equalsIgnoreCase(n.getName()))
                .findFirst();

        if (result.isEmpty()) {
            throw new CommandSyntaxException(COULD_NOT_FIND_NAMEABLE,
                    Component.literal("Could not find %s with name '%s'!".formatted(getType(), arg)));
        }

        return result.get();
    }

    @Override
    default <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(getNameables().stream().map(Nameable::getName), builder);
    }

    @Override
    default Collection<String> getExamples() {
        return getNameables()
                .stream()
                .limit(EXAMPLE_SIZE)
                .map(Nameable::getName)
                .collect(Collectors.toList());
    }

}
