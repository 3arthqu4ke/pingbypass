package me.earth.pingbypass.commons.protocol.play;

import io.netty.buffer.Unpooled;
import me.earth.pingbypass.commons.protocol.PbPacket;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import org.jetbrains.annotations.NotNull;

public abstract class S2CPacket extends ClientboundCustomPayloadPacket
        implements CustomPacket, PbPacket<ClientPlayHandler> {
    private static final ClientboundCustomPayloadPacket ID =
            new ClientboundCustomPayloadPacket(RESOURCE, new FriendlyByteBuf(Unpooled.buffer()));
    static {
        ID.getData().release();
    }

    private final int id;

    public S2CPacket(int id) {
        super(RESOURCE, new FriendlyByteBuf(Unpooled.buffer()));
        this.id = id;
    }

    @Override
    public final Integer getId() {
        return getProtocol().getPacketId(PacketFlow.CLIENTBOUND, ID);
    }

    @Override
    public final ConnectionProtocol getProtocol() {
        return ConnectionProtocol.PLAY;
    }

    @Override
    public final void write(@NotNull FriendlyByteBuf buf) {
        synchronized(this.getData()) {
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }
    }

}
