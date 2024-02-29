package me.earth.pingbypass.server.mixins.network;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface IClientPacketListener {
    @Accessor("serverChunkRadius")
    int getServerChunkRadius();

}
