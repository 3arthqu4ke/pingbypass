package me.earth.earthhack.impl.event.events;

import me.earth.earthhack.api.event.events.Event;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

/**
 * A cancellable Event,
 * wrapping a packet.
 *
 * @param <T> the type of packet.
 */
public class PacketEvent<T extends Packet<? extends INetHandler>> extends Event
{
    private final T packet;

    private PacketEvent(T packet)
    {
        this.packet = packet;
    }

    public T getPacket()
    {
        return packet;
    }

    /**
     * An object of this sub class gets created when
     * a packet is sent.
     *
     * @param <T> the type of packet sent.
     */
    public static class Send<T extends Packet<? extends INetHandler>> extends PacketEvent<T>
    {
        public Send(T packet)
        {
            super(packet);
        }
    }

    /**
     * An object of this sub class gets created when
     * a packet is received.
     *
     * @param <T> the type of packet received.
     */
    public static class Receive<T extends Packet<? extends INetHandler>> extends PacketEvent<T>
    {
        public Receive(T packet)
        {
            super(packet);
        }
    }

    /**
     * Exists only for Sending, not required for receiving.
     * Objects of this class will be created after a packet has
     * successfully been sent.
     *
     * @param <T> the type of packet sent.
     */
    public static class Post<T extends Packet<? extends INetHandler>> extends PacketEvent<T>
    {
        public Post(T packet)
        {
            super(packet);
        }
    }

}
