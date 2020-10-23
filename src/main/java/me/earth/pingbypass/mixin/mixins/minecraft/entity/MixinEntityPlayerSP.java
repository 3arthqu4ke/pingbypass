package me.earth.pingbypass.mixin.mixins.minecraft.entity;

import me.earth.pingbypass.mixin.ducks.IEntityPlayerSP;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP implements IEntityPlayerSP
{

    @Override
    @Accessor(value = "hasValidHealth")
    public abstract boolean hasValidHealth();
}
