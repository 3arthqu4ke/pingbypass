package me.earth.pingbypass.api.mixins.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Player.class)
public interface IPlayer {
    @Accessor("DATA_PLAYER_ABSORPTION_ID")
    static EntityDataAccessor<Float> getDataPlayerAbsorptionId() {
        throw new IllegalStateException("DATA_PLAYER_ABSORPTION_ID accessor has not been mixed in!");
    }

}
