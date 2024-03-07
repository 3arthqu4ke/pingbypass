package me.earth.pingbypass.api.mixins.gui;

import me.earth.pingbypass.api.ducks.network.IChatComponent;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.ListIterator;

@Mixin(ChatComponent.class)
public abstract class MixinChatComponent implements IChatComponent {
    @Shadow @Final private List<GuiMessage> allMessages;

    @Shadow protected abstract void refreshTrimmedMessage();

    @Override
    @Invoker("addMessage")
    public abstract void invokeAddMessage(Component component, @Nullable MessageSignature signature, int addedTime,
                          @Nullable GuiMessageTag messageTag, boolean refresh);

    @Override
    public void deleteImmediately(@Nullable MessageSignature signature, boolean all) {
        if (signature == null) {
            return;
        }

        ListIterator<GuiMessage> listIterator = this.allMessages.listIterator();
        boolean changed = false;
        while (listIterator.hasNext()) {
            GuiMessage message = listIterator.next();
            if (signature.equals(message.signature())) {
                listIterator.remove();
                changed = true;
                if (!all) {
                    break;
                }
            }
        }

        if (changed) {
            this.refreshTrimmedMessage();
        }
    }

}
