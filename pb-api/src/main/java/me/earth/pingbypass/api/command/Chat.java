package me.earth.pingbypass.api.command;

import net.minecraft.network.chat.Component;

public interface Chat {
    void send(Component message);

    void send(Component message, String identifier);

    void sendWithoutLogging(Component message);

    void sendWithoutLogging(Component message, String identifier);

    void delete(String identifier);

    default void send(String string) {
        this.send(Component.literal(string));
    }

}
