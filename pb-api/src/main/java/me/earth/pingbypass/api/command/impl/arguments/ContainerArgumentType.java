package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.JsonSerializationFunction;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.types.container.Container;
import me.earth.pingbypass.api.setting.impl.types.container.ContainerAction;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.commands.SharedSuggestionProvider.matchesSubStr;

@RequiredArgsConstructor
public class ContainerArgumentType<T extends Nameable> implements ArgumentType<Container<T>> {
    private static final DynamicCommandExceptionType INVALID_ACTION = new DynamicCommandExceptionType(
            (action) -> Component.literal("Invalid action: %s expected one of add, del or clear.".formatted(action)));

    private final JsonSerializationFunction<T> serializer;
    private final ArgumentType<T> typeParser;
    private final Collection<String> examples;
    private final Setting<Container<T>> setting;

    @Override
    public Container<T> parse(StringReader reader) throws CommandSyntaxException {
        String actionName = reader.readString();
        ContainerAction action = Nameable.getByNameIgnoreCase(Arrays.asList(ContainerAction.values()), actionName);
        if (action == null) {
            throw INVALID_ACTION.create(actionName);
        }

        T value = null;
        if (action != ContainerAction.CLEAR) {
            reader.expect(' ');
            value = typeParser.parse(reader);
        }

        return new Container<>(setting.getValue(), action, value, serializer);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String[] split = builder.getRemainingLowerCase().split(" ", 2);
        if (split.length == 1) {
            return SharedSuggestionProvider.suggest(Arrays.stream(ContainerAction.values()).map(Nameable::getName), builder);
        } else if (split.length >= 2) {
            ContainerAction action = ContainerAction.ADD;
            try {
                action = ContainerAction.valueOf(split[0].toUpperCase());
            } catch (IllegalArgumentException ignored) {
            }

            return suggest(context, builder, action, split);
        }

        return Suggestions.empty();
    }

    @SuppressWarnings("unused")
    protected <S> CompletableFuture<Suggestions> suggest(CommandContext<S> context, SuggestionsBuilder builder, ContainerAction action, String[] split) {
        SuggestionsBuilder builderForType = new SuggestionsBuilder(builder.getInput(), builder.getInput().toLowerCase(), builder.getStart() + split[0].length() + 1);
        switch (action) {
            case ADD -> {
                return typeParser.listSuggestions(context, builderForType);
            }
            case DEL -> {
                for (T nameable : setting.getValue()) {
                    if (matchesSubStr(split[1], nameable.getNameLowerCase())) {
                        builderForType.suggest(nameable.getName());
                    }
                }

                return builderForType.buildFuture();
            }
            default -> {}
        }

        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return examples;
    }

}
