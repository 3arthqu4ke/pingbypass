package me.earth.pingbypass.api.ducks.network;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.jetbrains.annotations.Nullable;

public interface IChatComponent {
    void invokeAddMessage(Component component, @Nullable MessageSignature signature, int addedTime,
                          @Nullable GuiMessageTag messageTag, boolean refresh);

    void deleteImmediately(@Nullable MessageSignature signature, boolean all);

}
