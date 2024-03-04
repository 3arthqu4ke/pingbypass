package me.earth.pingbypass.api.util.packet;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.PacketFlow;

public interface CustomPacket {
    Integer getId(ConnectionProtocol.CodecData<?> codecData);

    ConnectionProtocol getProtocol();

    PacketFlow getFlow();

    default boolean isValidPacket(ConnectionProtocol.CodecData<?> codecData) {
        return codecData.protocol().equals(getProtocol()) && codecData.flow().equals(getFlow());
    }

}
