package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PseudoLiteral implements ArgumentType<String> {
    private static final Dynamic2CommandExceptionType EXCEPTION = new Dynamic2CommandExceptionType(
            (s,o) -> Component.literal("Unknown option %s. Available options: %s".formatted(s, o)));

    private final List<String> list;
    private final String examples;

    public PseudoLiteral(String... strings) {
        this.list = Collections.unmodifiableList(Arrays.asList(strings));
        this.examples = "(%s)".formatted(String.join("|", strings));
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return list.stream()
                .filter(s -> s.equalsIgnoreCase(reader.getRemaining()))
                .findFirst()
                .orElseThrow(() -> EXCEPTION.create(reader.getRemaining(), examples));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        list.forEach(builder::suggest);
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return list;
    }

}
