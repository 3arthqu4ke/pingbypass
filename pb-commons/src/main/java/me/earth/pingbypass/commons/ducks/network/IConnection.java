package me.earth.pingbypass.commons.ducks.network;

import me.earth.pingbypass.commons.event.network.PacketEvent;
import net.minecraft.network.protocol.Packet;

public interface IConnection {
    void pingbypass$send(Packet<?> packet);

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

}
