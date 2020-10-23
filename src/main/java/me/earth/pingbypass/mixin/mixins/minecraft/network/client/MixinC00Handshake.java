package me.earth.pingbypass.mixin.mixins.minecraft.network.client;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(C00Handshake.class)
public abstract class MixinC00Handshake
{
    @Shadow
    private String ip;

    @Redirect(method = "writePacketData", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketBuffer;writeString(Ljava/lang/String;)Lnet/minecraft/network/PacketBuffer;"))
    public PacketBuffer writeStringHook(PacketBuffer buffer, String string)
    {
        return buffer.writeString(this.ip);
    }

}
