package me.earth.pingbypass.server.event;

import lombok.Getter;
import me.earth.pingbypass.commons.event.network.PacketEvent;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.protocol.Packet;

@Getter
public class PbPacketEvent<P extends Packet<?>> extends PacketEvent<P> {
    private final Session session;

    protected PbPacketEvent(P packet, Session session) {
        super(packet, session);
        this.session = session;
    }

    public static class C2Pb<P extends Packet<?>> extends PbPacketEvent<P> {
        public C2Pb(P packet, Session session) {
            super(packet, session);
        }
    }

    public static class Pb2C<P extends Packet<?>> extends PbPacketEvent<P> {
        public Pb2C(P packet, Session session) {
            super(packet, session);
        }
    }

}
