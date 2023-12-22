package me.earth.pingbypass.commons.module.render;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.commons.event.CancellingListener;
import me.earth.pingbypass.commons.event.render.ParticleEvent;
import net.minecraft.core.particles.ParticleTypes;

public class NoRender extends ModuleImpl {
    public NoRender(PingBypass pingBypass) {
        super(pingBypass, "NoRender", Categories.RENDER, "Disables certain animations.");
        register("Fire", "Prevents fire from rendering.", Fire.class);
        register("Totems", "Stops the totem animation.", Totems.class);
        register("Blocks", "Prevents the block overlay from rendering.", Blocks.class);
        register("Skylight", "Prevents Skylight updates.", Skylight.class);
        register("Liquids", "Prevents the liquid overlay from rendering.", Liquids.class);
        register("Hurtcam", "Stops your screen from shaking when you are hurt.", Hurt.class);
        register("Bob", "Stops your screen from bobbing when you move.", Bob.class);
        register("Fov", "Stops all effects changing your FOV.", Fov.class);
        Setting<Boolean> explosions = bool("Explosions", true, "Removes explosion particles.");
        Setting<Boolean> particles = bool("Particles", false, "Removes all particles.");
        listen(new Listener<ParticleEvent>() {
            @Override
            public void onEvent(ParticleEvent event) {
                if (particles.getValue() || explosions.getValue() && (event.getType().equals(ParticleTypes.EXPLOSION) || event.getType().equals(ParticleTypes.EXPLOSION_EMITTER))) {
                    event.setCancelled(true);
                }
            }
        });
    }

    private <T extends CancellableEvent> void register(String name, String description, Class<T> eventType) {
        Setting<Boolean> setting = bool(name, true, description);
        listen(new CancellingListener.WithSetting<>(eventType, setting));
    }

    public static final class Fire extends CancellableEvent {}
    public static final class Totems extends CancellableEvent {}
    public static final class Skylight extends CancellableEvent {}
    public static final class Blocks extends CancellableEvent {}
    public static final class Liquids extends CancellableEvent {}
    public static final class Hurt extends CancellableEvent {}
    public static final class Bob extends CancellableEvent {}
    public static final class Fov extends CancellableEvent {}

}
