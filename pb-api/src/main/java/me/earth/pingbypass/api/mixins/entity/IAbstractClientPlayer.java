package me.earth.pingbypass.api.mixins.entity;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractClientPlayer.class)
public interface IAbstractClientPlayer {
    @Invoker("getPlayerInfo")
    PlayerInfo invokeGetPlayerInfo();

}
