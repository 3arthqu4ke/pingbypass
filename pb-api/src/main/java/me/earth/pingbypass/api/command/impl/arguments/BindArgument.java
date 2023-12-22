package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.input.*;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.SharedSuggestionProvider.matchesSubStr;

@RequiredArgsConstructor
public class BindArgument implements ArgumentType<Bind> {
    private static final DynamicCommandExceptionType INVALID_KEY = new DynamicCommandExceptionType(
            (name) -> Component.literal("Could not find key: " + name));

    private final Collection<String> examples;
    private final KeyboardAndMouse registry;

    public BindArgument(KeyboardAndMouse registry) {
        this.registry = registry;
        this.examples = registry.stream().map(Key::getName).toList();
    }

    @Override
    public Bind parse(StringReader reader) throws CommandSyntaxException {
        List<String> strings = new ArrayList<>(2);
        while (reader.canRead()) {
            strings.add(reader.readString());
            if (reader.canRead() && reader.peek() == ',') {
                reader.read();
            } else {
                break;
            }
        }

        Set<Key> keys = new HashSet<>((int) (strings.size() * 1.5));
        for (String string : strings) {
            Key key = registry.getKeyByName(string.toUpperCase());
            if (key == null) {
                throw INVALID_KEY.create(string);
            }

            if (key.getCode() != Keys.KEY_NONE) {
                keys.add(key);
            }
        }

        return keys.isEmpty() ? Bind.none() : new Bind(keys);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String[] split = builder.getRemainingLowerCase().split(",");
        if (split.length == 0) {
            return Suggestions.empty();
        }

        int length = 0;
        for (int i = 0; i < split.length - 1; i++) {
            length += split[i].length() + 1;
        }

        SuggestionsBuilder builderForLastKey = new SuggestionsBuilder(builder.getInput(), builder.getInput().toLowerCase(), builder.getStart() + length);
        for (Key key : registry) {
            if (matchesSubStr(split[split.length - 1], key.getNameLowerCase())) {
                builderForLastKey.suggest(key.getName());
            }
        }

        return builderForLastKey.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }

    public static BindArgument empty() {
        return new BindArgument(DummyKeyboardAndMouse.INSTANCE);
    }

}
