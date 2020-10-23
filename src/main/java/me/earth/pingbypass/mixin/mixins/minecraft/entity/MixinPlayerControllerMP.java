package me.earth.pingbypass.mixin.mixins.minecraft.entity;

import me.earth.earthhack.impl.mixin.ducks.IPlayerControllerMP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP implements IPlayerControllerMP
{

    @Override
    @Invoker(value = "syncCurrentPlayItem")
    public abstract void syncItem();

    @Override
    @Accessor(value = "currentPlayerItem")
    public abstract int getItem();

}
