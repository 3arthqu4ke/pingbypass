package me.earth.pingbypass.api.event.loop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import net.minecraft.client.player.LocalPlayer;

@Getter
@RequiredArgsConstructor
public class LocalPlayerUpdateEvent extends CancellableEvent {
    private final LocalPlayer player;

}
