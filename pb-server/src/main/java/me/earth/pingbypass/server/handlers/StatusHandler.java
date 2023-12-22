package me.earth.pingbypass.server.handlers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.*;
import org.jetbrains.annotations.NotNull;

/**
 * A custom {@link net.minecraft.server.network.ServerStatusPacketListenerImpl}.
 */
@RequiredArgsConstructor
public class StatusHandler implements ServerStatusPacketListener, IHandler {
    private static final Component DISCONNECT_REASON = Component.translatable("multiplayer.status.request_handled");
    private final PingBypassServer server;
    @Getter
    private final Session session;
    private boolean hasRequestedStatus;

    @Override
    public void handleStatusRequest(@NotNull ServerboundStatusRequestPacket serverboundStatusRequestPacket) {
        if (hasRequestedStatus) {
            server.getSessionManager().disconnect(session, DISCONNECT_REASON);
        } else {
            hasRequestedStatus = true;
            session.send(new ClientboundStatusResponsePacket(server.getServerStatusService().getServerStatus()));
        }
    }

    @Override
    public void handlePingRequest(ServerboundPingRequestPacket serverboundPingRequestPacket) {
        session.send(new ClientboundPongResponsePacket(serverboundPingRequestPacket.getTime()));
        server.getSessionManager().disconnect(session, DISCONNECT_REASON);
    }

}
