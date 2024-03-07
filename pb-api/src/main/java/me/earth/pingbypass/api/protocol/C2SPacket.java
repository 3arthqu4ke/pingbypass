package me.earth.pingbypass.api.protocol;

import me.earth.pingbypass.api.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;

/**
 * Base class for packets sent from the PingBypassClient to the PingBypassServer.
 */
public interface C2SPacket extends CustomPacket, PBPacket {
    ServerboundCustomPayloadPacket ID = new ServerboundCustomPayloadPacket(ProtocolManager.PAYLOAD);

    @Override
    default void write(FriendlyByteBuf buffer) {
        new ServerboundCustomPayloadPacket(new Payload(this)).write(buffer);
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
        return PacketFlow.SERVERBOUND;
    }

}
