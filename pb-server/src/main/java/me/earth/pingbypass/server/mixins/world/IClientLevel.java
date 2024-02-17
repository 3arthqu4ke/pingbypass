package me.earth.pingbypass.server.mixins.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ClientLevel.class)
public interface IClientLevel {
    @Invoker("getEntities")
    LevelEntityGetter<Entity> invokeGetEntities();

}
