package me.earth.pingbypass.api.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.earth.pingbypass.api.registry.Registry;

public interface CommandManager extends Registry<Command>, ProvidesSuggestions<CommandSource> {
    void execute(String command, CommandSource source) throws CommandSyntaxException;

    String getPrefix();

    void setPrefix(String prefix);

}
