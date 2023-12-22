package me.earth.pingbypass.commons.protocol.play;

import io.netty.buffer.Unpooled;
import me.earth.pingbypass.commons.protocol.PbPacket;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import org.jetbrains.annotations.NotNull;

public abstract class C2SPacket extends ServerboundCustomPayloadPacket
        implements CustomPacket, PbPacket<ServerPlayHandler> {
    private static final ServerboundCustomPayloadPacket ID =
            new ServerboundCustomPayloadPacket(RESOURCE, new FriendlyByteBuf(Unpooled.buffer()));
    static {
        ID.getData().release();
    }

    private final int id;

    public C2SPacket(int id) {
        super(RESOURCE, new FriendlyByteBuf(Unpooled.buffer()));
        this.id = id;
    }

    @Override
    public final Integer getId() {
        return getProtocol().getPacketId(PacketFlow.SERVERBOUND, ID);
    }

    @Override
    public final ConnectionProtocol getProtocol() {
        return ConnectionProtocol.PLAY;
    }

    @Override
    public final void write(@NotNull FriendlyByteBuf buf) {
        synchronized (this.getData()) {
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }
    }

}
