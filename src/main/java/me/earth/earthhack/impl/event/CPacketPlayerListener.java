package me.earth.earthhack.impl.event;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * Subscribing to all CPacketPlayers is a tedious process
 * so this is a listener that makes that process easier.
 * Just add all the listeners to your Subscribers listeners
 * or subscribe this subscriber to the bus.
 */
public abstract class CPacketPlayerListener extends SubscriberImpl
{
    public CPacketPlayerListener()
    {
        Listener<PacketEvent.Send<CPacketPlayer>> packetListener = new EventListener<PacketEvent.Send<CPacketPlayer>>(PacketEvent.Send.class, CPacketPlayer.class)
        {
            @Override
            public void invoke(PacketEvent.Send<CPacketPlayer> event)
            {
                onPacket(event);
            }
        };
        this.listeners.add(packetListener);
        Listener<PacketEvent.Send<CPacketPlayer.Position>> positionListener = new EventListener<PacketEvent.Send<CPacketPlayer.Position>>(PacketEvent.Send.class, CPacketPlayer.Position.class)
        {
            @Override
            public void invoke(PacketEvent.Send<CPacketPlayer.Position> event)
            {
                onPosition(event);
            }
        };
        this.listeners.add(positionListener);
        Listener<PacketEvent.Send<CPacketPlayer.Rotation>> rotationListener = new EventListener<PacketEvent.Send<CPacketPlayer.Rotation>>(PacketEvent.Send.class, CPacketPlayer.Rotation.class)
        {
            @Override
            public void invoke(PacketEvent.Send<CPacketPlayer.Rotation> event)
            {
                onRotation(event);
            }
        };
        this.listeners.add(rotationListener);
        Listener<PacketEvent.Send<CPacketPlayer.PositionRotation>> positionRotationListener = new EventListener<PacketEvent.Send<CPacketPlayer.PositionRotation>>(PacketEvent.Send.class, CPacketPlayer.PositionRotation.class)
        {
            @Override
            public void invoke(PacketEvent.Send<CPacketPlayer.PositionRotation> event)
            {
                onPositionRotation(event);
            }
        };
        this.listeners.add(positionRotationListener);
    }

    protected abstract void onPacket(PacketEvent<CPacketPlayer> event);

    protected abstract void onPosition(PacketEvent<CPacketPlayer.Position> event);

    protected abstract void onRotation(PacketEvent<CPacketPlayer.Rotation> event);

    protected abstract void onPositionRotation(PacketEvent<CPacketPlayer.PositionRotation> event);

}
