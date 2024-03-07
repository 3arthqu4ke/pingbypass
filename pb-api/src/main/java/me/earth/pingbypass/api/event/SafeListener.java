package me.earth.pingbypass.api.event;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.util.exceptions.NullabilityUtil;
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
        NullabilityUtil.safe(mc, ((player, level, gameMode) -> onEvent(event, player, level, gameMode)));
    }

}
