package me.earth.pingbypass.api.mixins.network;

import me.earth.pingbypass.api.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConnectionProtocol.CodecData.class)
public abstract class MixinConnectionProtocolCodecData {
    @Inject(method = "packetId", at = @At("HEAD"), cancellable = true)
    public void packetIdHook(Packet<?> packet, CallbackInfoReturnable<@Nullable Integer> cir) {
        if (packet instanceof CustomPacket customPacket) {
            cir.setReturnValue(customPacket.getId(ConnectionProtocol.CodecData.class.cast(this)));
        }
    }

    @Inject(method = "isValidPacketType", at = @At("HEAD"), cancellable = true)
    public void isValidPacketTypeHook(Packet<?> packet, CallbackInfoReturnable<Boolean> cir) {
        if (packet instanceof CustomPacket customPacket) {
            cir.setReturnValue(customPacket.isValidPacket(ConnectionProtocol.CodecData.class.cast(this)));
        }
    }

}
