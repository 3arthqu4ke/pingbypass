package me.earth.pingbypass.api.command.impl.builder;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A custom {@link com.mojang.brigadier.builder.RequiredArgumentBuilder} with some convenience methods.
 *
 * @param <S> type of the command source.
 * @param <T> type of the argument.
 */
@Getter
@RequiredArgsConstructor
public class ExtendedRequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, ExtendedRequiredArgumentBuilder<S, T>>
        implements ExecutesSuccessfulCommand<S, ExtendedRequiredArgumentBuilder<S, T>> {
    private SuggestionProvider<S> suggestionsProvider = null;
    private final ArgumentType<T> type;
    private final String name;

    @Override
    protected ExtendedRequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    @Override
    public ArgumentCommandNode<S, T> build() {
        ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(
                getName(), getType(), getCommand(), getRequirement(), getRedirect(),
                getRedirectModifier(), isFork(), getSuggestionsProvider());

        for (CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

    public ExtendedRequiredArgumentBuilder<S, T> suggests(SuggestionProvider<S> provider) {
        this.suggestionsProvider = provider;
        return getThis();
    }

}
