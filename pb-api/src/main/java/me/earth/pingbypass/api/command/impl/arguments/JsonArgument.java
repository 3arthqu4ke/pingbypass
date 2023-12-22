package me.earth.pingbypass.api.command.impl.arguments;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class JsonArgument implements ArgumentType<JsonElement> {
    private static final DynamicCommandExceptionType INVALID_JSON = new DynamicCommandExceptionType(
        (message) -> Component.literal("Invalid json: " + message));

    private final ArgumentType<String> delegate = StringArgument.greedy("json");

    @Override
    public JsonElement parse(StringReader reader) throws CommandSyntaxException {
        String string = delegate.parse(reader);
        try {
            return JsonParser.parseString(string);
        } catch (JsonParseException e) {
            throw INVALID_JSON.create(e.getMessage());
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return delegate.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return delegate.getExamples();
    }

}
