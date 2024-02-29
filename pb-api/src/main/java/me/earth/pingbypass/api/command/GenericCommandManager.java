package me.earth.pingbypass.api.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.RootCommandNode;
import me.earth.pingbypass.api.registry.Registry;

public interface GenericCommandManager<S, C extends GenericCommand<S>> extends Registry<C>, ProvidesSuggestions<S> {
    void execute(String command, S source) throws CommandSyntaxException;

    RootCommandNode<S> getRoot();

    String getPrefix();

    void setPrefix(String prefix);

}
