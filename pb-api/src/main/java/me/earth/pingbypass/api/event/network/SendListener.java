package me.earth.pingbypass.api.event.network;

import lombok.NoArgsConstructor;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.listeners.generic.GenericListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;

/**
 * @param <P> the type of packet processed by this listener, most likely for the {@link ServerGamePacketListener}, but not guaranteed.
 */
public abstract class SendListener<P extends Packet<?>> extends GenericListener<PacketEvent.Send<P>> {
    public SendListener() {
        this(EventListener.DEFAULT_LISTENER_PRIORITY);
    }

    public SendListener(int priority) {
        super(PacketEvent.Send.class, priority);
    }

    @NoArgsConstructor
    public static abstract class Direct<P extends Packet<?>> extends SendListener<P> {
        public Direct(int priority) {
            super(priority);
        }

        @Override
        public void onEvent(PacketEvent.Send<P> event) {
            onPacket(event.getPacket());
        }

        public abstract void onPacket(P packet);
    }

}
