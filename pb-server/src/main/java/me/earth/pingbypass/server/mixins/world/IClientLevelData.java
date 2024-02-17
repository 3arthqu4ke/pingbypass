package me.earth.pingbypass.server.mixins.world;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.ClientLevelData.class)
public interface IClientLevelData {
    @Accessor("isFlat")
    boolean isIsFlat();

}
