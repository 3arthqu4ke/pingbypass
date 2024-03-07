package me.earth.pingbypass.server.handlers.play;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.AbstractEventListener;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents the flow of packets that come from the server to the pingbypass and are then to be send to the client.
 */
@Slf4j
public class S2PB2CPipeline extends SubscriberImpl {
    private final List<Packet<?>> packets = new CopyOnWriteArrayList<>();
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Session session;

    public S2PB2CPipeline(Session session) {
        this.session = session;
        listen(new AbstractEventListener.Unsafe<PacketEvent.Receive<?>>(PacketEvent.Receive.class, Integer.MIN_VALUE) {
            @Override
            public void onEvent(PacketEvent.Receive<?> event) {
                if (event.getConnection().pingbypass$getReceiving() == PacketFlow.SERVERBOUND || event.isCancelled() || event.isCancelledForPB()) {
                    return;
                }

                synchronized (locked) {
                    if (locked.get()) {
                        packets.add(event.getPacket());
                    } else {
                        send(session, event.getPacket());
                    }
                }
            }
        });

        doNotProxy(ClientboundPingPacket.class);
        doNotProxy(ClientboundKeepAlivePacket.class);
    }

    @Synchronized("locked")
    public void lock() {
        locked.set(true);
    }

    @Synchronized("locked")
    public void unlockAndFlush() {
        for (Packet<?> packet : packets) {
            send(session, packet);
        }

        packets.clear();
        locked.set(false);
    }

    private <P extends Packet<?>> void send(Session session, P packet) {
        PipelineEvent<P> event = new PipelineEvent<>(session, packet);
        session.getServer().getEventBus().post(event, packet.getClass());
        Packet<?> packetToSend = event.getPacket();
        if (event.getPacket() != null) {
            session.send(packetToSend);
        }
    }

    private void doNotProxy(Class<? extends Packet<?>> type) {
        listen(new AbstractEventListener.Unsafe<PacketEvent.Receive<?>>(PacketEvent.Receive.class, type) {
            @Override
            public void onEvent(PacketEvent.Receive<?> event) {
                event.setCancelledForPB(true);
            }
        });
    }

}
