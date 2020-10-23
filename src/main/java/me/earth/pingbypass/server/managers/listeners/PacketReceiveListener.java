package me.earth.pingbypass.server.managers.listeners;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.pingbypass.server.managers.SPacketManager;

public class PacketReceiveListener extends EventListener<PacketEvent.Receive<?>>
{
    private final SPacketManager manager;

    public PacketReceiveListener(SPacketManager manager)
    {
        super(PacketEvent.Receive.class, Integer.MAX_VALUE - 1);
        this.manager = manager;
    }

    @Override
    public void invoke(PacketEvent.Receive<?> event)
    {
        manager.onPacketReceive(event);
    }

}
