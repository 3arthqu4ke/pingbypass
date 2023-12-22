package me.earth.pingbypass.commons.command.components;

import net.minecraft.network.chat.ClickEvent;

public class DynamicClickEvent extends ClickEvent {
    public DynamicClickEvent() {
        super(Action.SUGGEST_COMMAND, "This should not happen! Please inform a PingBypass developer!");
    }



}
