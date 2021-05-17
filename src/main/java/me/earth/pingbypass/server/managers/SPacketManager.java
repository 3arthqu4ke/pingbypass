package me.earth.pingbypass.server.managers;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.pingbypass.server.PhobosServer;
import me.earth.pingbypass.server.managers.listeners.PacketReceiveListener;
import me.earth.pingbypass.server.managers.listeners.PacketSendListener;
import me.earth.pingbypass.util.wrappers.CPacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.login.server.SPacketEnableCompression;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.network.login.server.SPacketLoginSuccess;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketKeepAlive;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages the packets between proxy and server.
 * Sends packets coming from the server to the client
 * and blocks packets that the minecraft client
 * underneath the proxy would send.
 */
public class SPacketManager extends SubscriberImpl implements Globals
{
    private final Set<Class<? extends Packet<?>>> cPackets = new HashSet<>();
    private final Set<Class<? extends Packet<?>>> sPackets = new HashSet<>();
    private final PhobosServer server;

    public SPacketManager(PhobosServer server)
    {
        this.server = server;

        cPackets.add(CPacketWrapper.class);
        cPackets.add(CPacketLoginStart.class);
        cPackets.add(C00Handshake.class);
        cPackets.add(CPacketEncryptionResponse.class);
        cPackets.add(CPacketKeepAlive.class);
        // Add authorized packets like in last Pingbypass?
        // For now these are the only ones we need tho.
        cPackets.add(CPacketChatMessage.class);
        cPackets.add(CPacketUseEntity.class);
        cPackets.add(CPacketPlayerTryUseItemOnBlock.class);
        cPackets.add(CPacketAnimation.class);
        cPackets.add(CPacketClickWindow.class);
        cPackets.add(CPacketConfirmTransaction.class);
        cPackets.add(CPacketResourcePackStatus.class);

        sPackets.add(SPacketEncryptionRequest.class);
        sPackets.add(SPacketEnableCompression.class);
        sPackets.add(SPacketLoginSuccess.class);
        sPackets.add(SPacketKeepAlive.class);

        this.listeners.add(new PacketReceiveListener(this));
        this.listeners.add(new PacketSendListener(this));
    }

    public void onPacketReceive(PacketEvent.Receive<?> event)
    {
        Packet<?> packet = event.getPacket();
        // Quick and dirty fix for AntiPingSpoof
        if (packet instanceof SPacketConfirmTransaction)
        {
            SPacketConfirmTransaction p = (SPacketConfirmTransaction) packet;
            if (!p.wasAccepted()
                    && (p.getWindowId() == 0
                            ? mc.player.inventoryContainer
                            : mc.player.openContainer) != null)
            {
                mc.player
                  .connection
                  .sendPacket(
                    new CPacketConfirmTransaction(p.getWindowId(),
                                                  p.getActionNumber(),
                                                  true));
                event.setCancelled(true);
                return;
            }
        }

        if (!sPackets.contains(packet.getClass()))
        {
            server.sendToClient(packet);
        }
    }

    public void onPacketSend(PacketEvent.Send<?> event)
    {
        if (!cPackets.contains(event.getPacket().getClass()))
        {
            event.setCancelled(true);
        }
    }

}
