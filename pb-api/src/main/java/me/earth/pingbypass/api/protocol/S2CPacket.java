package me.earth.pingbypass.api.protocol;

import me.earth.pingbypass.api.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;

/**
 * Base class for packets sent from the PingBypassServer to the PingBypassClient.
 */
public interface S2CPacket extends CustomPacket, PBPacket {
    ClientboundCustomPayloadPacket ID = new ClientboundCustomPayloadPacket(ProtocolManager.PAYLOAD);

    @Override
    default void write(FriendlyByteBuf buffer) {
        new ClientboundCustomPayloadPacket(new Payload(this)).write(buffer);
    }

    @Override
    default Integer getId(ConnectionProtocol.CodecData<?> codecData) {
        return codecData.packetId(ID);
    }

    @Override
    default ConnectionProtocol getProtocol() {
        return ConnectionProtocol.PLAY;
    }

    @Override
    default PacketFlow getFlow() {
        return PacketFlow.CLIENTBOUND;
    }

}
