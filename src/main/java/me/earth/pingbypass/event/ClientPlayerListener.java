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
 * Analogue to {@link me.earth.earthhack.impl.event.CPacketPlayerListener},
 * just for steveice's library packets.
 */
public abstract class ClientPlayerListener extends SubscriberImpl
{
    public ClientPlayerListener()
    {
        Listener<CPacketEvent.Send<ClientPlayerMovementPacket>> packetListener = new EventListener<CPacketEvent.Send<ClientPlayerMovementPacket>>(CPacketEvent.Send.class, ClientPlayerMovementPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Send<ClientPlayerMovementPacket> event)
            {
                onPacket(event);
            }
        };
        this.listeners.add(packetListener);
        Listener<CPacketEvent.Send<ClientPlayerPositionPacket>> positionListener = new EventListener<CPacketEvent.Send<ClientPlayerPositionPacket>>(CPacketEvent.Send.class, ClientPlayerPositionPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Send<ClientPlayerPositionPacket> event)
            {
                onPosition(event);
            }
        };
        this.listeners.add(positionListener);
        Listener<CPacketEvent.Send<ClientPlayerRotationPacket>> rotationListener = new EventListener<CPacketEvent.Send<ClientPlayerRotationPacket>>(CPacketEvent.Send.class, ClientPlayerRotationPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Send<ClientPlayerRotationPacket> event)
            {
                onRotation(event);
            }
        };
        this.listeners.add(rotationListener);
        Listener<CPacketEvent.Send<ClientPlayerPositionRotationPacket>> positionRotationListener = new EventListener<CPacketEvent.Send<ClientPlayerPositionRotationPacket>>(CPacketEvent.Send.class, ClientPlayerPositionRotationPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Send<ClientPlayerPositionRotationPacket> event)
            {
                onPositionRotation(event);
            }
        };
        this.listeners.add(positionRotationListener);
    }

    protected abstract void onPacket(CPacketEvent.Send<ClientPlayerMovementPacket> event);

    protected abstract void onPosition(CPacketEvent.Send<ClientPlayerPositionPacket> event);

    protected abstract void onRotation(CPacketEvent.Send<ClientPlayerRotationPacket> event);

    protected abstract void onPositionRotation(CPacketEvent.Send<ClientPlayerPositionRotationPacket> event);
}
