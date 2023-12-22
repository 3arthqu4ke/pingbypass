package me.earth.pingbypass.server.mixins;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Main.class)
public abstract class MixinMain {
    // this is here because it's the earliest InjectionPoint where we can load our Mixin config plugin
}
