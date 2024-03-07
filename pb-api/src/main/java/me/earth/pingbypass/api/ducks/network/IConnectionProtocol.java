package me.earth.pingbypass.api.ducks.network;

import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

public interface IConnectionProtocol {
    int pingbypass_getId(PacketFlow packetFlow, Class<? extends Packet<?>> packet);

    @SuppressWarnings("DataFlowIssue")
    static int getId(ConnectionProtocol protocol, PacketFlow packetFlow, Class<? extends Packet<?>> packet) {
        return IConnectionProtocol.class.cast(protocol).pingbypass_getId(packetFlow, packet);
    }

}
