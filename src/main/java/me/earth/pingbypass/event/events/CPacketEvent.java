package me.earth.pingbypass.event.events;

import com.github.steveice10.packetlib.packet.Packet;
import me.earth.earthhack.api.event.events.Event;

public class CPacketEvent<T extends Packet> extends Event
{
    private final T packet;

    private CPacketEvent(T packet)
    {
        this.packet = packet;
    }

    public T getPacket()
    {
        return packet;
    }

    /**
     * Similar to Receive the naming scheme is a bit confusing here,
     * this event will be fired when we receive a packet from our actual client,
     * which we will probably redirect and send to the actual server hence the name.
     *
     * @param <T>
     */
    public static class Send<T extends Packet> extends CPacketEvent<T>
    {
        public Send(T packet)
        {
            super(packet);
        }
    }

    /**
     * The naming scheme seems a bit confusing here, but this
     * event will be fired when we send a packet our actual client
     * which will receive the packet, hence the name.
     *
     * @param <T>
     */
    public static class Receive<T extends Packet> extends CPacketEvent<T>
    {
        public Receive(T packet)
        {
            super(packet);
        }
    }

    /**
     * This Event will be fired after the packet has been
     * sent to the actual server. Together with CPacketEvent.Send this
     * allows us to simulate onUpdateWalkingPlayer pre and post for the client.
     *
     * @param <T>
     */
    public static class Post<T extends Packet> extends CPacketEvent<T>
    {
        public Post(T packet)
        {
            super(packet);
        }
    }

}
