package me.earth.pingbypass.server.managers.listeners;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.pingbypass.server.managers.SPacketManager;

public class PacketSendListener extends EventListener<PacketEvent.Send<?>>
{
    private final SPacketManager manager;

    public PacketSendListener(SPacketManager manager)
    {
        super(PacketEvent.Send.class, Integer.MAX_VALUE - 1);
        this.manager = manager;
    }

    @Override
    public void invoke(PacketEvent.Send<?> event)
    {
        manager.onPacketSend(event);
    }

}
