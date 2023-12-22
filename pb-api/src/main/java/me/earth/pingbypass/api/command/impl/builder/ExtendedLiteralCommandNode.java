package me.earth.pingbypass.api.command.impl.builder;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import lombok.experimental.Delegate;
import me.earth.pingbypass.api.command.impl.CommandManagerImpl;
import me.earth.pingbypass.api.command.impl.suggestions.CommandSuggestionProvider;

import java.util.function.Predicate;

/**
 * A {@link LiteralCommandNode} with a {@link SuggestionProvider}. This e.g. allows us to use the
 * {@link CommandSuggestionProvider} to display a tooltip for the first arg of the {@link CommandManagerImpl}s
 * {@link CommandDispatcher}, something not possible normally.
 *
 * @param <S> the type of the command source.
 */
public class ExtendedLiteralCommandNode<S> extends LiteralCommandNode<S> {
    @Delegate
    private final SuggestionProvider<S> suggestionProvider;

    public ExtendedLiteralCommandNode(String literal, Command<S> command, Predicate<S> requirement,
                                      CommandNode<S> redirect, RedirectModifier<S> modifier, boolean forks,
                                      SuggestionProvider<S> suggestionProvider) {
        super(literal, command, requirement, redirect, modifier, forks);
        this.suggestionProvider = suggestionProvider == null ? super::listSuggestions : suggestionProvider;
    }

}
