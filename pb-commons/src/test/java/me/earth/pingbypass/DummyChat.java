package me.earth.pingbypass;

import me.earth.pingbypass.api.command.Chat;
import net.minecraft.network.chat.Component;

public class DummyChat implements Chat {
    @Override
    public void send(Component message) {
        System.out.println(message.getString());
    }

    @Override
    public void send(Component message, String identifier) {
        System.out.println(message.getString());
    }

    @Override
    public void sendWithoutLogging(Component message) {

    }

    @Override
    public void sendWithoutLogging(Component message, String identifier) {

    }

    @Override
    public void delete(String identifier) {

    }

}
