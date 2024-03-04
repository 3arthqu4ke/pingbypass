package me.earth.pingbypass.api.protocol;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.api.protocol.event.CustomPayloadInitEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@Slf4j
public class ProtocolListener extends SubscriberImpl {
    private final ProtocolManager protocolManager;
    private final PacketFlow flow;

    public ProtocolListener(ProtocolManager protocolManager, PacketFlow flow) {
        this.protocolManager = protocolManager;
        this.flow = flow;
        listen(new Listener<CustomPayloadInitEvent>() {
            @Override
            public void onEvent(CustomPayloadInitEvent event) {
                onCustomPayloadInit(event);
            }
        });

        listen(new Listener<PacketEvent.Receive<ServerboundCustomPayloadPacket>>() {
            @Override
            public void onEvent(PacketEvent.Receive<ServerboundCustomPayloadPacket> event) {
                onPacketReceived(event.getPacket().payload(), event);
            }
        });

        listen(new Listener<PacketEvent.Receive<ClientboundCustomPayloadPacket>>() {
            @Override
            public void onEvent(PacketEvent.Receive<ClientboundCustomPayloadPacket> event) {
                onPacketReceived(event.getPacket().payload(), event);
            }
        });
    }

    protected void onCustomPayloadInit(CustomPayloadInitEvent event) {
        PBPacket packet;
        if ((packet = protocolManager.handle(event.getLocation(), event.getBuf())) != null) {
            event.setPayload(new PBPacket.Payload(packet));
        }
    }

    protected void onPacketReceived(CustomPacketPayload payload, PacketEvent.Receive<?> event) {
        if (flow == event.getConnection().pingbypass$getReceiving() && onPayload(payload, event.getConnection().getAsConnection())) {
            event.setCancelled(true);
            event.setCancelledForPB(true);
        }
    }

    protected boolean onPayload(CustomPacketPayload payload, Connection connection) {
        return payload instanceof PBPacket.Payload packetPayload && protocolManager.handle(connection, packetPayload.packet());
    }

}
