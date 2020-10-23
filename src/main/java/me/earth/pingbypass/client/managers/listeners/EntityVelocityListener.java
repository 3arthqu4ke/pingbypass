package me.earth.pingbypass.client.managers.listeners;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class EntityVelocityListener extends EventListener<PacketEvent.Receive<SPacketEntityVelocity>>
{
    public EntityVelocityListener()
    {
        super(PacketEvent.Receive.class, Integer.MIN_VALUE, SPacketEntityVelocity.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketEntityVelocity> event)
    {
        event.setCancelled(true);
    }

}
