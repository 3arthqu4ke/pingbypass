package me.earth.pingbypass.server.mixins.player;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LocalPlayer.class)
public interface ILocalPlayer {
    @Invoker("getPermissionLevel")
    int invokeGetPermissionLevel();

}
