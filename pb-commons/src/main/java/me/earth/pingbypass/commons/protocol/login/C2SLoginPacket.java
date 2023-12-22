package me.earth.pingbypass.commons.protocol.login;

import io.netty.buffer.Unpooled;
import me.earth.pingbypass.commons.protocol.PbPacket;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import org.jetbrains.annotations.NotNull;

public abstract class C2SLoginPacket extends ServerboundCustomQueryPacket
        implements CustomPacket, PbPacket<ServerLoginHandler> {
    private static final ServerboundCustomQueryPacket ID =
            new ServerboundCustomQueryPacket(0, new FriendlyByteBuf(Unpooled.buffer()));
    static {
        //noinspection DataFlowIssue
        ID.getData().release();
    }

    private final int id;

    public C2SLoginPacket(int id, int transactionId) {
        super(transactionId, new FriendlyByteBuf(Unpooled.buffer()));
        this.id = id;
    }

    @Override
    public final Integer getId() {
        return getProtocol().getPacketId(PacketFlow.SERVERBOUND, ID);
    }

    @Override
    public final ConnectionProtocol getProtocol() {
        return ConnectionProtocol.LOGIN;
    }

    @Override
    public final void write(@NotNull FriendlyByteBuf buf) {
        //noinspection DataFlowIssue
        synchronized(this.getData()) {
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }
    }

}
