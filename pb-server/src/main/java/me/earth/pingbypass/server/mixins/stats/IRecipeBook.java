package me.earth.pingbypass.server.mixins.stats;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(RecipeBook.class)
public interface IRecipeBook {
    @Accessor("known")
    Set<ResourceLocation> getKnown();

    @Accessor("highlight")
    Set<ResourceLocation> getHighlight();

}
