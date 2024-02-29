package me.earth.pingbypass.api.event.render;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import net.minecraft.core.particles.ParticleType;

@Getter
@RequiredArgsConstructor
public class ParticleEvent extends CancellableEvent {
    private final ParticleType<?> type;

}
