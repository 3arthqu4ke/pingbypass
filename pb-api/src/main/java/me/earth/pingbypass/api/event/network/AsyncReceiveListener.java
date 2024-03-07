package me.earth.pingbypass.api.event.network;

import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.listeners.generic.GenericListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.Packet;

public abstract class AsyncReceiveListener<P extends Packet<?>> extends GenericListener<PacketEvent.Receive<P>> {
    private final Minecraft mc;

    public AsyncReceiveListener(int priority, Minecraft mc) {
        super(PacketEvent.Receive.class, priority);
        this.mc = mc;
    }

    public AsyncReceiveListener(Minecraft mc) {
        this(EventListener.DEFAULT_LISTENER_PRIORITY, mc);
    }

    public abstract void onEvent(PacketEvent.Receive<P> event, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode);

    @Override
    public void onEvent(PacketEvent.Receive<P> event) {
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        MultiPlayerGameMode gameMode = mc.gameMode;
        if (player != null && level != null && gameMode != null) {
            this.onEvent(event, player, level, gameMode);
        }
    }

}
