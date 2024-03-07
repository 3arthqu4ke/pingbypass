package me.earth.pingbypass.api.event.network;

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
public abstract class PostListener<P extends Packet<?>> extends GenericListener<PacketEvent.PostReceive<P>> {
    protected final Minecraft mc;

    public PostListener(int priority, Minecraft mc) {
        super(PacketEvent.PostReceive.class, priority);
        this.mc = mc;
    }

    public PostListener(Minecraft mc) {
        this(EventListener.DEFAULT_LISTENER_PRIORITY, mc);
    }

    public abstract void onEventMainthread(PacketEvent.PostReceive<P> event);

    @Override
    public void onEvent(PacketEvent.PostReceive<P> event) {
        mc.submit(() -> onEventMainthread(event));
    }

    public static abstract class Safe<P extends Packet<?>> extends PostListener<P> {
        public Safe(int priority, Minecraft mc) {
            super(priority, mc);
        }

        public Safe(Minecraft mc) {
            super(mc);
        }

        public abstract void onSafeEvent(PacketEvent.PostReceive<P> event, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode);

        @Override
        public void onEventMainthread(PacketEvent.PostReceive<P> event) {
            LocalPlayer player = mc.player;
            ClientLevel level = mc.level;
            MultiPlayerGameMode gameMode = mc.gameMode;
            if (player != null && level != null) {
                this.onSafeEvent(event, player, level, gameMode);
            }
        }

        public static abstract class Direct<P extends Packet<?>> extends Safe<P> {
            public Direct(int priority, Minecraft mc) {
                super(priority, mc);
            }

            public Direct(Minecraft mc) {
                super(mc);
            }

            public abstract void onSafePacket(P packet, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode);

            @Override
            public void onSafeEvent(PacketEvent.PostReceive<P> event, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode) {
                onSafePacket(event.getPacket(), player, level, gameMode);
            }
        }
    }

}
