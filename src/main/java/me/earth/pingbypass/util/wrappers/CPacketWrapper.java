package me.earth.pingbypass.util.wrappers;

import com.github.steveice10.packetlib.packet.Packet;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

/**
 * Wraps a packet from SteveIce's library
 * into a minecraft packet.
 */
@SuppressWarnings("NullableProblems")
public class CPacketWrapper implements net.minecraft.network.Packet<INetHandler>
{
    private final Packet packet;

    public CPacketWrapper(Packet packetIn)
    {
        packet = packetIn;
    }

    @Override
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        /* This packet should only be written.*/
    }

    @Override
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        /* {@link me.earth.pingbypass.mixin.mixins.minecraft.network.MixinNettyPacketEncoder}. */
    }

    @Override
    public void processPacket(INetHandler iNetHandler)
    {
        /* This packet should only be written.*/
    }

    public Packet getPacket()
    {
        return packet;
    }

}
