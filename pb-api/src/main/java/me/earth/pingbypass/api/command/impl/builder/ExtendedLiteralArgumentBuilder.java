package me.earth.pingbypass.api.command.impl.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.Getter;

/**
 * A {@link LiteralArgumentBuilder} but building a {@link ExtendedLiteralCommandNode} with a
 * {@link SuggestionProvider}. This allows us to display tooltips for our commands.
 * Also has convenience methods from {@link ExecutesSuccessfulCommand} so that we do not have to return
 * {@link Command#SINGLE_SUCCESS} on every command execution.
 *
 * @param <S> the type of the command source.
 */
@Getter
public class ExtendedLiteralArgumentBuilder<S> extends LiteralArgumentBuilder<S>
        implements ExecutesSuccessfulCommand<S, LiteralArgumentBuilder<S>> {
    private SuggestionProvider<S> suggestionProvider;

    public ExtendedLiteralArgumentBuilder(String literal) {
        super(literal);
    }

    public ExtendedLiteralArgumentBuilder<S> withSuggestionProvider(SuggestionProvider<S> suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
        return this;
    }

    @Override
    public LiteralCommandNode<S> build() {
        ExtendedLiteralCommandNode<S> result = new ExtendedLiteralCommandNode<>(
                getLiteral(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork(),
                getSuggestionProvider());

        for (CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

    public static <S> ExtendedLiteralArgumentBuilder<S> literal(String name) {
        return new ExtendedLiteralArgumentBuilder<S>(name);
    }

}
