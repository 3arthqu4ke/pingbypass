package me.earth.pingbypass.api.command;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.command.Chat;
import me.earth.pingbypass.api.ducks.network.IChatComponent;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;

import org.jetbrains.annotations.Nullable;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class ChatImpl implements Chat {
    private final Minecraft mc;

    @Override
    public void send(Component message) {
        mc.gui.getChat().addMessage(message);
    }

    @Override
    public void send(Component message, String identifier) {
        send(mc.gui.getChat(), message, identifier);
    }

    @Override
    public void sendWithoutLogging(Component message) {
        sendWithoutLogging((IChatComponent) mc.gui.getChat(), message, null);
    }

    @Override
    public void sendWithoutLogging(Component message, String identifier) {
        sendWithoutLogging((IChatComponent) mc.gui.getChat(), message, identifier);
    }

    @Override
    public void delete(String identifier) {
        delete(new MessageSignature(get256Bytes(identifier)));
    }

    private void send(ChatComponent chat, Component message, @Nullable String identifier) {
        var signature = identifier != null ? new MessageSignature(get256Bytes(identifier)) : null;
        delete(signature);
        chat.addMessage(message, signature, GuiMessageTag.system());
    }

    private void sendWithoutLogging(IChatComponent chat, Component message, @Nullable String identifier) {
        var signature = identifier != null ? new MessageSignature(get256Bytes(identifier)) : null;
        delete(signature);
        chat.invokeAddMessage(message, signature, mc.gui.getGuiTicks(), GuiMessageTag.system(), false);
    }

    private void delete(@Nullable MessageSignature signature) {
        ((IChatComponent) mc.gui.getChat()).deleteImmediately(signature, true);
    }

    private byte[] get256Bytes(String identifier) {
        byte[] bytes = new byte[256];
        byte[] identifierBytes = identifier.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(identifierBytes, 0, bytes, 0, Math.min(bytes.length, identifierBytes.length));
        return bytes;
    }

}
