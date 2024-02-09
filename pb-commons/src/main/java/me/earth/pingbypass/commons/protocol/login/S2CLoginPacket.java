package me.earth.pingbypass.commons.protocol.login;

import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.commons.protocol.PbPacket;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.custom.CustomQueryPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class S2CLoginPacket implements CustomPacket, Packet<ClientLoginHandler>, PbPacket<ClientLoginHandler> {
    private static final ClientboundCustomQueryPacket ID = new ClientboundCustomQueryPacket(0, new CustomQueryPayload() {
        @Override
        public @NotNull ResourceLocation id() {
            return new ResourceLocation("");
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buffer) {

        }
    });

    private final int transactionId;
    private final int id;

    @Override
    public final Integer getId() {
        return getProtocol().codec(PacketFlow.CLIENTBOUND).packetId(ID);
    }

    @Override
    public final ConnectionProtocol getProtocol() {
        return ConnectionProtocol.LOGIN;
    }

    @Override
    public final void write(@NotNull FriendlyByteBuf buf) {
        /*synchronized(this.getData()) { // This may be access multiple times, from multiple threads, lets be safe.
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }*/
    }

    @Override
    public void handle(ClientLoginHandler handler) {

    }
}
