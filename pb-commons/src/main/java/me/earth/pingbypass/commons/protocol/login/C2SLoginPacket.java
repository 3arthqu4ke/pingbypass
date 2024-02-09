package me.earth.pingbypass.commons.protocol.login;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.commons.protocol.PbPacket;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class C2SLoginPacket implements CustomPacket, Packet<ServerLoginHandler>, PbPacket<ServerLoginHandler> {
    private static final ServerboundCustomQueryAnswerPacket ID = new ServerboundCustomQueryAnswerPacket(0, null);

    private final int transactionId;
    private final int id;

    @Override
    public final Integer getId() {
        return getProtocol().codec(PacketFlow.SERVERBOUND).packetId(ID);
    }

    @Override
    public final ConnectionProtocol getProtocol() {
        return ConnectionProtocol.LOGIN;
    }

    @Override
    public final void write(@NotNull FriendlyByteBuf buf) {
        //noinspection DataFlowIssue
        /*synchronized(this.getData()) {
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }*/
    }

    @Override
    public void handle(ServerLoginHandler handler) {

    }

}
