package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EnumArgument<T extends Enum<T>> implements ArgumentType<T> {
    private static final Dynamic2CommandExceptionType INVALID_ENUM = new Dynamic2CommandExceptionType(
        (found, constants) -> Component.literal(
                "Could not find %s in %s".formatted(found, Arrays.toString((Object[]) constants))));

    private final Collection<String> examples;
    private final Class<T> type;

    public EnumArgument(Class<T> type) {
        this.examples = Arrays.stream(type.getEnumConstants()).limit(3).map(Enum::name).collect(Collectors.toList());
        this.type = type;
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        return Arrays
                .stream(type.getEnumConstants())
                .filter(t -> t.name().equalsIgnoreCase(reader.getRemaining()))
                .findFirst()
                .orElseThrow(() -> INVALID_ENUM.create(reader.getRemaining(), type.getEnumConstants()));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(Arrays.stream(type.getEnumConstants()).map(Enum::name), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }

}
