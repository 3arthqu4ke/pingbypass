package me.earth.pingbypass.api.mixins.network.c2s;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.protocol.event.CustomPayloadInitEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinServerboundCustomPayloadPacket {
    @Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
    private static void readPayloadHook(ResourceLocation id, FriendlyByteBuf buffer, CallbackInfoReturnable<CustomPacketPayload> cir) {
        CustomPayloadInitEvent event = new CustomPayloadInitEvent(ServerboundCustomPayloadPacket.class, id, buffer);
        PingBypassApi.getEventBus().post(event);
        CustomPacketPayload payload = event.getPayload();
        if (payload != null) {
            cir.setReturnValue(payload);
        }
    }

}
