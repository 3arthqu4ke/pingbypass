package me.earth.pingbypass.server.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.earth.pingbypass.commons.ducks.network.IConnection;
import me.earth.pingbypass.commons.event.network.PacketEvent;
import me.earth.pingbypass.server.event.PbPacketEvent;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class Session extends Connection implements IConnection {
    @Setter(AccessLevel.PRIVATE)
    private ConnectionProtocol connectionProtocol = ConnectionProtocol.HANDSHAKING;
    @Setter(AccessLevel.PACKAGE)
    private boolean primarySession;
    private boolean completed;
    private String userName;
    private boolean admin;
    private UUID uuid;
    private String id;

    public Session() {
        super(PacketFlow.SERVERBOUND);
    }

    @Override
    public void setProtocol(@NotNull ConnectionProtocol connectionProtocol) {
        this.connectionProtocol = connectionProtocol;
        super.setProtocol(connectionProtocol);
    }

    @Override
    public void pingbypass$send(Packet<?> packet) {
        this.send(packet);
    }

    @Override
    public PacketEvent<?> getSendEvent(Packet<?> packet) {
        return new PbPacketEvent.Pb2C<>(packet, this);
    }

    @Override
    public PacketEvent<?> getReceiveEvent(Packet<?> packet) {
        return new PbPacketEvent.C2Pb<>(packet, this);
    }

}
