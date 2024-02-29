package me.earth.pingbypass.api.module.misc;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;
import me.earth.pingbypass.api.event.gui.GuiScreenEvent;
import net.minecraft.client.gui.screens.DeathScreen;

public class AutoRespawn extends ModuleImpl {
    public AutoRespawn(PingBypass pingBypass) {
        super(pingBypass, "AutoRespawn", Categories.MISC, "Automatically respawns you when you die.");
        listen(new Listener<GuiScreenEvent<DeathScreen>>() {
            @Override
            public void onEvent(GuiScreenEvent<DeathScreen> event) {
                respawn(event);
            }
        });

        listen(new Listener<DeathScreenEvent>() {
            @Override
            public void onEvent(DeathScreenEvent event) {
                respawn(event);
            }
        });
    }

    private void respawn(CancellableEvent event) {
        if (mc.player != null) {
            mc.player.respawn();
        }

        event.setCancelled(true);
    }

    public static final class DeathScreenEvent extends CancellableEvent { }

}
