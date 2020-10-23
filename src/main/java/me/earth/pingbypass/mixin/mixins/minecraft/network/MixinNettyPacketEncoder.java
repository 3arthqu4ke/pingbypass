package me.earth.pingbypass.mixin.mixins.minecraft.network;

import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.tcp.io.ByteBufNetOutput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.earth.pingbypass.util.packets.PacketUtil;
import me.earth.pingbypass.util.wrappers.CPacketWrapper;
import net.minecraft.network.NettyPacketEncoder;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NettyPacketEncoder.class)
public abstract class MixinNettyPacketEncoder
{
    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(method = "encode", at = @At("HEAD"), cancellable = true)
    protected void encodeHook(ChannelHandlerContext handler, Packet<?> packetIn, ByteBuf buf, CallbackInfo info)
    {
        if (packetIn instanceof CPacketWrapper)
        {
            encodeWrapper((CPacketWrapper) packetIn, buf);
            info.cancel();
        }
    }

    private void encodeWrapper(CPacketWrapper wrapper, ByteBuf buf)
    {
        com.github.steveice10.packetlib.packet.Packet packet = wrapper.getPacket();

        int packetID = PacketUtil.getPacketID(packet);

        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeVarInt(packetID); //hmm had issues without it. But maybe the issue was somewhere else I forgot.
        NetOutput out = new ByteBufNetOutput(buf);

        try
        {
            packet.write(out);
        }
        catch (Throwable throwable)
        {
            LOGGER.error(throwable);
        }
    }

}
