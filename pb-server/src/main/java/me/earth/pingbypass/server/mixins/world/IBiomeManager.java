package me.earth.pingbypass.server.mixins.world;

import net.minecraft.world.level.biome.BiomeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeManager.class)
public interface IBiomeManager {
    @Accessor("biomeZoomSeed")
    long getBiomeZoomSeed();

}
