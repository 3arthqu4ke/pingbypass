package me.earth.pingbypass.api.event;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;

@RequiredArgsConstructor
public abstract class SafeListener<E> extends Listener<E> {
    protected final Minecraft mc;

    public SafeListener(Minecraft mc, int priority) {
        super(priority);
        this.mc = mc;
    }

    public abstract void onEvent(E event, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode);

    @Override
    public void onEvent(E event) {
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        MultiPlayerGameMode gameMode = mc.gameMode;
        if (player != null && level != null && gameMode != null) {
            onEvent(event, player, level, gameMode);
        }
    }

}
