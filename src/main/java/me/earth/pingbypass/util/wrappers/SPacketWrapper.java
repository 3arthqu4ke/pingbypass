package me.earth.pingbypass.util.wrappers;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import net.minecraft.network.Packet;

import java.io.IOException;

/**
 * Wraps a Minecraft packet to one from
 * SteveIce's library.
 */
public class SPacketWrapper extends MinecraftPacket
{
    private final Packet<?> packet;

    public SPacketWrapper(Packet<?> packetIn)
    {
        packet = packetIn;
    }

    @Override
    public void read(NetInput in) throws IOException
    {
        /* This packet should only be written. */
    }

    @Override
    public void write(NetOutput out) throws IOException
    {
        /* {@link me.earth.pingbypass.mixin.mixins.library.MixinTcpPacketCodec} */
    }

    public Packet<?> getPacket()
    {
        return packet;
    }

}
