package me.earth.pingbypass.api.command.impl.util;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

@UtilityClass
public class ComponentUtil {
    public static <T extends Nameable & HasDescription> MutableComponent getComponent(T t, ChatFormatting formatting) {
        return Component
                .literal(t.getName())
                .withStyle(style -> style
                        .withColor(formatting)
                        .withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT, Component.literal(t.getDescription()))));
    }

    // TODO: make every Setting return its own component!!!!!
    public static ChatFormatting getColor(Object value) {
        if (value instanceof Boolean bool) {
            return bool ? ChatFormatting.GREEN : ChatFormatting.RED;
        } else if (value instanceof Number) {
            return ChatFormatting.AQUA;
        } else if (value instanceof String) {
            return ChatFormatting.GOLD;
        }

        return ChatFormatting.WHITE;
    }

    public static Component getSimpleValueComponent(Object value) {
        return Component.literal(String.valueOf(value))
                .withStyle(ComponentUtil.getColor(value));
    }

}
