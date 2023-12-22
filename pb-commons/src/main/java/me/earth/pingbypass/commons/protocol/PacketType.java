package me.earth.pingbypass.commons.protocol;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.commons.ducks.network.IConnectionProtocol;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;

import static net.minecraft.network.ConnectionProtocol.LOGIN;
import static net.minecraft.network.ConnectionProtocol.PLAY;
import static net.minecraft.network.protocol.PacketFlow.CLIENTBOUND;
import static net.minecraft.network.protocol.PacketFlow.SERVERBOUND;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PacketType {
    public static final PacketType S2C_LOGIN = new PacketType(LOGIN, CLIENTBOUND, ClientboundCustomQueryPacket.class);
    public static final PacketType C2S_LOGIN = new PacketType(LOGIN, SERVERBOUND, ServerboundCustomQueryPacket.class);
    public static final PacketType S2C_PLAY = new PacketType(PLAY, CLIENTBOUND, ClientboundCustomPayloadPacket.class);
    public static final PacketType C2S_PLAY = new PacketType(PLAY, SERVERBOUND, ServerboundCustomPayloadPacket.class);

    private final ConnectionProtocol connectionProtocol;
    private final PacketFlow packetFlow;
    private final int packetId;

    private PacketType(ConnectionProtocol protocol, PacketFlow flow, Class<? extends Packet<?>> packet) {
        this(protocol, flow, IConnectionProtocol.getId(LOGIN, CLIENTBOUND, packet));
    }

}
