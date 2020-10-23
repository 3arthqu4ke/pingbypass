package me.earth.pingbypass.server.managers;

import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.PacketSendingEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.event.events.Event;
import me.earth.earthhack.api.util.Globals;
import me.earth.pingbypass.event.events.CPacketEvent;
import me.earth.pingbypass.server.PhobosClient;
import me.earth.pingbypass.server.nethandler.NetHandlerPlayPhobos;

/**
 * Manages the packets between client and proxy.
 * Handles the packets from the client (e.g. setting
 * the players position after a CPacketPlayer.Position is
 * received) and resends them to the server we play on.
 */
public class CPacketManager extends SessionAdapter implements Globals
{
    private final NetHandlerPlayPhobos netHandler = new NetHandlerPlayPhobos();
    private final PhobosClient client;

    public CPacketManager(PhobosClient client)
    {
        this.client = client;
    }

    @Override
    public void packetReceived(PacketReceivedEvent event)
    {
        Packet packet = event.getPacket();
        if (netHandler.handlePacket(packet))
        {
            CPacketEvent<?> packetEvent = new CPacketEvent.Send<>(packet);
            postEvent(packetEvent, packet.getClass());

            if (!packetEvent.isCancelled())
            {
                client.sendToServer(packet);
            }
        }
    }

    @Override
    public void packetSending(PacketSendingEvent event)
    {
        Packet packet = event.getPacket();
        CPacketEvent<?> packetEvent = new CPacketEvent.Receive<>(packet);
        postEvent(packetEvent, packet.getClass());

        event.setCancelled(packetEvent.isCancelled());
    }

    private void postEvent(Event event, Class<?> type)
    {
        try
        {
            Bus.EVENT_BUS.post(event, type);
        }
        catch (Exception e)
        {
            event.setCancelled(false);
        }
    }

}
