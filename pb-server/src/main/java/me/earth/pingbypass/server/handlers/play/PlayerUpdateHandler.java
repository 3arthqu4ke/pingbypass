package me.earth.pingbypass.server.handlers.play;

import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.event.loop.LocalPlayerUpdateEvent;
import net.minecraft.client.player.LocalPlayer;

public class PlayerUpdateHandler extends SubscriberImpl {
    private final ThreadLocal<Boolean> inUpdate = ThreadLocal.withInitial(() -> false);

    public PlayerUpdateHandler() {
        listen(new Listener<LocalPlayerUpdateEvent>() {
            @Override
            public void onEvent(LocalPlayerUpdateEvent event) {
                if (!inUpdate.get()) {
                    event.setCancelled(true);
                }
            }
        });
    }

    public void update(LocalPlayer player) {
        try {
            inUpdate.set(true);
            player.tick();
        } finally {
            inUpdate.set(false);
        }
    }

}
