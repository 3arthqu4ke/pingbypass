package me.earth.pingbypass.api.mixins.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.protocol.Packet;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PacketEncoder.class)
public abstract class MixinPacketEncoder {
    @Final
    @Shadow
    private static Logger LOGGER;

    @Inject(
        method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;Lio/netty/buffer/ByteBuf;)V",
        at = @At(value = "NEW", target = "(Ljava/lang/String;)Ljava/io/IOException;", remap = false),
        locals = LocalCapture.CAPTURE_FAILHARD)
    private void encodeHook(ChannelHandlerContext ctx, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci,
                            Attribute<ConnectionProtocol.CodecData<?>> attribute, ConnectionProtocol.CodecData<?> codecData, int id) {
        LOGGER.info("Can't serialize unregistered packet: {}, Codec: {}:{}", packet, codecData.protocol().id(), codecData.flow());
    }

}
