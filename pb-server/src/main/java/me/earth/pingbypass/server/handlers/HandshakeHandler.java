package me.earth.pingbypass.server.handlers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;

/**
 * A custom {@link net.minecraft.server.network.ServerHandshakePacketListenerImpl}
 */
@RequiredArgsConstructor
public class HandshakeHandler implements ServerHandshakePacketListener, IHandler {
    private static final Component IGNORE_STATUS_REASON = Component.literal("Ignoring status request");

    private final PingBypassServer server;
    @Getter
    private final Session session;

    @Override
    public void handleIntention(ClientIntentionPacket packet) {
        switch (packet.intention()) {
            case LOGIN -> {
                session.setClientboundProtocolAfterHandshake(ClientIntent.LOGIN);
                if (packet.protocolVersion() != SharedConstants.getCurrentVersion().getProtocolVersion()) {
                    Component component;
                    if (packet.protocolVersion() < 754) {
                        component = Component.translatable("multiplayer.disconnect.outdated_client", SharedConstants.getCurrentVersion().getName());
                    } else {
                        component = Component.translatable("multiplayer.disconnect.incompatible", SharedConstants.getCurrentVersion().getName());
                    }

                    server.getSessionManager().disconnect(session, component);
                } else {
                    session.setListener(new LoginHandler(server, session));
                }
            }
            case STATUS -> {
                if (this.server.getServerConfig().get(ServerConstants.REPLY_TO_STATUS)) {
                    session.setClientboundProtocolAfterHandshake(ClientIntent.STATUS);
                    session.setListener(new StatusHandler(server, session));
                } else {
                    server.getSessionManager().disconnect(session, IGNORE_STATUS_REASON);
                }
            }
            default -> throw new UnsupportedOperationException("Invalid intention " + packet.intention());
        }
    }

    @Override
    public void onDisconnect(Component reason) {

    }

}
