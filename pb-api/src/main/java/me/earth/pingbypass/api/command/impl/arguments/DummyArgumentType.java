package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public enum DummyArgumentType implements ArgumentType<Object> {
    INSTANCE;

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        throw new UnsupportedOperationException("This is a dummy argument type");
    }

    @Override
    public Collection<String> getExamples() {
        throw new UnsupportedOperationException("This is a dummy argument type");
    }

    @Override
    public Object parse(StringReader reader) throws CommandSyntaxException {
        throw new UnsupportedOperationException("This is a dummy argument type");
    }

    @SuppressWarnings("unchecked")
    public static <T> ArgumentType<T> dummy() {
        return (ArgumentType<T>) INSTANCE;
    }

}
