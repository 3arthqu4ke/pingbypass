package me.earth.pingbypass.commons.event.network;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.commons.ducks.network.IConnection;
import net.minecraft.network.protocol.Packet;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PacketEvent<P extends Packet<?>> extends CancellableEvent {
    private final P packet;
    private final IConnection connection;

    public static class Send<P extends Packet<?>> extends PacketEvent<P> {
        public Send(P packet, IConnection connection) {
            super(packet, connection);
        }
    }

    public static class PostSend<P extends Packet<?>> extends PacketEvent<P> {
        public PostSend(P packet, IConnection connection) {
            super(packet, connection);
        }
    }

    public static class Receive<P extends Packet<?>> extends PacketEvent<P> {
        public Receive(P packet, IConnection connection) {
            super(packet, connection);
        }
    }

    public static class PostReceive<P extends Packet<?>> extends PacketEvent<P> {
        public PostReceive(P packet, IConnection connection) {
            super(packet, connection);
        }
    }

}
