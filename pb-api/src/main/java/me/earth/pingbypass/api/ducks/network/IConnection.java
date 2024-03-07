package me.earth.pingbypass.api.ducks.network;

import me.earth.pingbypass.api.event.network.PacketEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;

/**
 * Duck interface for {@link Connection}
 */
public interface IConnection {
    /**
     * @param packet the packet to send.
     * @see Connection#send(Packet)
     */
    void pingbypass$send(Packet<?> packet);

    /**
     * @return {@link Connection#getReceiving()}
     */
    PacketFlow pingbypass$getReceiving();

    default PacketEvent<?> getSendEvent(Packet<?> packet) {
        return new PacketEvent.Send<>(packet, this);
    }

    default PacketEvent<?> getPostSendEvent(Packet<?> packet) {
        return new PacketEvent.PostSend<>(packet, this);
    }

    default PacketEvent<?> getReceiveEvent(Packet<?> packet) {
        return new PacketEvent.Receive<>(packet, this);
    }

    default PacketEvent<?> getPostReceiveEvent(Packet<?> packet) {
        return new PacketEvent.PostReceive<>(packet, this);
    }

    default Connection getAsConnection() {
        return (Connection) this;
    }

}
