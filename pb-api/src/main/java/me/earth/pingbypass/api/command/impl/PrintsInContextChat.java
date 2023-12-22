package me.earth.pingbypass.api.command.impl;

import com.mojang.brigadier.context.CommandContext;
import me.earth.pingbypass.api.command.CommandSource;
import net.minecraft.network.chat.Component;

public interface PrintsInContextChat {
    default void print(CommandContext<CommandSource> context, Component message) {
        context.getSource().getChat().send(message);
    }

}
