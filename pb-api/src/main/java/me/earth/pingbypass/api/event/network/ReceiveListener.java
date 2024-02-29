package me.earth.pingbypass.api.event.network;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.listeners.generic.GenericListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

/**
 * @param <P> the type of packet processed by this listener, most likely for the {@link ClientGamePacketListener}, but not guaranteed.
 */
public abstract class ReceiveListener<P extends Packet<?>> extends GenericListener<PacketEvent.Receive<P>> {
    public ReceiveListener() {
        this(EventListener.DEFAULT_LISTENER_PRIORITY);
    }

    public ReceiveListener(int priority) {
        super(PacketEvent.Receive.class, priority);
    }

    /**
     * @param <P> the type of packet processed by this listener, most likely for the {@link ClientGamePacketListener}, but not guaranteed.
     */
    @NoArgsConstructor
    public static abstract class Direct<P extends Packet<?>> extends ReceiveListener<P> {
        @Override
        public void onEvent(PacketEvent.Receive<P> event) {
            onPacket(event.getPacket());
        }

        public abstract void onPacket(P packet);
    }

    @RequiredArgsConstructor
    public static abstract class Scheduled<P extends Packet<?>> extends ReceiveListener<P> {
        protected final Minecraft mc;

        public Scheduled(Minecraft mc, int priority) {
            super(priority);
            this.mc = mc;
        }

        public abstract void onEventMainThread(PacketEvent.Receive<P> event);

        @Override
        public void onEvent(PacketEvent.Receive<P> event) {
            mc.submit(() -> onEventMainThread(event));
        }

        public static abstract class Safe<P extends Packet<?>> extends Scheduled<P> {
            public Safe(Minecraft mc) {
                super(mc);
            }

            public Safe(Minecraft mc, int priority) {
                super(mc, priority);
            }

            public abstract void onSafeEvent(PacketEvent.Receive<P> event, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode);

            @Override
            public void onEventMainThread(PacketEvent.Receive<P> event) {
                LocalPlayer player = mc.player;
                ClientLevel level = mc.level;
                MultiPlayerGameMode gameMode = mc.gameMode;
                if (player != null && level != null) {
                    this.onSafeEvent(event, player, level, gameMode);
                }
            }
        }
    }

}
