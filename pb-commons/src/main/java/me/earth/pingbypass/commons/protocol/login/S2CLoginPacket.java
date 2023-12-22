package me.earth.pingbypass.commons.protocol.login;

import io.netty.buffer.Unpooled;
import me.earth.pingbypass.commons.protocol.PbPacket;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import org.jetbrains.annotations.NotNull;

public abstract class S2CLoginPacket extends ClientboundCustomQueryPacket
        implements CustomPacket, PbPacket<ClientLoginHandler> {
    private static final ClientboundCustomQueryPacket ID =
            new ClientboundCustomQueryPacket(0, RESOURCE, new FriendlyByteBuf(Unpooled.buffer()));
    static {
        ID.getData().release();
    }

    private final int id;

    public S2CLoginPacket(int id, int transactionId) {
        super(transactionId, RESOURCE, new FriendlyByteBuf(Unpooled.buffer()));
        this.id = id;
    }

    @Override
    public final Integer getId() {
        return getProtocol().getPacketId(PacketFlow.CLIENTBOUND, ID);
    }

    @Override
    public final ConnectionProtocol getProtocol() {
        return ConnectionProtocol.LOGIN;
    }

    @Override
    public final void write(@NotNull FriendlyByteBuf buf) {
        synchronized(this.getData()) { // This may be access multiple times, from multiple threads, lets be safe.
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }
    }

}
