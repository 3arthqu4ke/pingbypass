package me.earth.pingbypass.event;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.pingbypass.event.events.CPacketEvent;

/**
 * Same as {@link me.earth.earthhack.impl.event.CPacketPlayerListener},
 * but for after the packet has been sent.
 */
public abstract class ClientPlayerPostListener extends SubscriberImpl
{
    public ClientPlayerPostListener()
    {
        Listener<CPacketEvent.Post<ClientPlayerMovementPacket>> packetListener = new EventListener<CPacketEvent.Post<ClientPlayerMovementPacket>>(CPacketEvent.Post.class, ClientPlayerMovementPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Post<ClientPlayerMovementPacket> event)
            {
                onPacket(event);
            }
        };
        this.listeners.add(packetListener);
        Listener<CPacketEvent.Post<ClientPlayerPositionPacket>> positionListener = new EventListener<CPacketEvent.Post<ClientPlayerPositionPacket>>(CPacketEvent.Post.class, ClientPlayerPositionPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Post<ClientPlayerPositionPacket> event)
            {
                onPosition(event);
            }
        };
        this.listeners.add(positionListener);
        Listener<CPacketEvent.Post<ClientPlayerRotationPacket>> rotationListener = new EventListener<CPacketEvent.Post<ClientPlayerRotationPacket>>(CPacketEvent.Post.class, ClientPlayerRotationPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Post<ClientPlayerRotationPacket> event)
            {
                onRotation(event);
            }
        };
        this.listeners.add(rotationListener);
        Listener<CPacketEvent.Post<ClientPlayerPositionRotationPacket>> positionRotationListener = new EventListener<CPacketEvent.Post<ClientPlayerPositionRotationPacket>>(CPacketEvent.Post.class, ClientPlayerPositionRotationPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Post<ClientPlayerPositionRotationPacket> event)
            {
                onPositionRotation(event);
            }
        };
        this.listeners.add(positionRotationListener);
    }

    protected abstract void onPacket(CPacketEvent.Post<ClientPlayerMovementPacket> event);

    protected abstract void onPosition(CPacketEvent.Post<ClientPlayerPositionPacket> event);

    protected abstract void onRotation(CPacketEvent.Post<ClientPlayerRotationPacket> event);

    protected abstract void onPositionRotation(CPacketEvent.Post<ClientPlayerPositionRotationPacket> event);
}
