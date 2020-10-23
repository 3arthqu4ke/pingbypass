package me.earth.pingbypass.mixin.mixins.library;

import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpPacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.earth.pingbypass.util.packets.PacketUtil;
import me.earth.pingbypass.util.wrappers.SPacketWrapper;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TcpPacketCodec.class, remap = false)
public abstract class MixinTcpPacketCodec
{
    @Inject(method = "encode", at = @At("HEAD"), cancellable = true)
    public void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf, CallbackInfo info)
    {
        if (packet instanceof SPacketWrapper)
        {
            encodeWrapper((SPacketWrapper) packet, buf);
            info.cancel();
        }
    }

    private void encodeWrapper(SPacketWrapper wrapper, ByteBuf buf)
    {
        net.minecraft.network.Packet<?> packet = wrapper.getPacket();

        int id = PacketUtil.getPacketID(packet);

        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeVarInt(id);

        try
        {
            packet.writePacketData(packetBuffer);
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }

}
