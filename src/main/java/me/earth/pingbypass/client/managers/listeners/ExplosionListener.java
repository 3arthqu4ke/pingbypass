package me.earth.pingbypass.client.managers.listeners;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import net.minecraft.network.play.server.SPacketExplosion;

public class ExplosionListener extends EventListener<PacketEvent.Receive<SPacketExplosion>>
{
    public ExplosionListener()
    {
        super(PacketEvent.Receive.class, Integer.MIN_VALUE, SPacketExplosion.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketExplosion> event)
    {
        event.setCancelled(true);
    }

}
