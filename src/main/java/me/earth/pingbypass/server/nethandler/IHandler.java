package me.earth.pingbypass.server.nethandler;

import com.github.steveice10.packetlib.packet.Packet;
import me.earth.earthhack.api.util.Globals;

/**
 * Handler used by {@link NetHandlerPlayPhobos}.
 *
 * @param <T> type of packet.
 */
public interface IHandler<T extends Packet> extends Globals
{

    /**
     * Handles a Packet received from the client,
     * returns <tt>true</tt> if the packet can be
     * send to the server.
     *
     * @param packet the packet.
     * @return <tt>true</tt> if sendable.
     */
    boolean handle(T packet);

}
