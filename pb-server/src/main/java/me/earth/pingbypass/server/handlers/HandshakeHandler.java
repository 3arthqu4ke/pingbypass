package me.earth.pingbypass.server.handlers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.commons.Constants;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A custom {@link net.minecraft.server.network.ServerHandshakePacketListenerImpl}
 */
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class HandshakeHandler implements ServerHandshakePacketListener, IHandler {
    private static final Component IGNORE_STATUS_REASON = Component.literal("Ignoring status request");

    private final PingBypassServer server;
    @Getter
    private final Session session;

    @Override
    public void handleIntention(ClientIntentionPacket packet) {
        /*switch (packet.getIntention()) {
            case LOGIN -> {
                session.setProtocol(ConnectionProtocol.LOGIN);
                if (packet.getProtocolVersion() != Constants.MC_PROTOCOL) {
                    MutableComponent component;
                    if (packet.getProtocolVersion() < 754) {
                        component = Component.translatable("multiplayer.disconnect.outdated_client", Constants.MC);
                    } else {
                        component = Component.translatable("multiplayer.disconnect.incompatible", Constants.MC);
                    }

                    server.getSessionManager().disconnect(session, component);
                } else {
                    session.setListener(new LoginHandler(server, session));
                }
            }
            case STATUS -> {
                if (this.server.getServerConfig().get(ServerConstants.REPLY_TO_STATUS)) {
                    session.setProtocol(ConnectionProtocol.STATUS);
                    session.setListener(new StatusHandler(server, session));
                } else {
                    server.getSessionManager().disconnect(session, IGNORE_STATUS_REASON);
                }
            }
            default -> throw new UnsupportedOperationException("Invalid intention " + packet.getIntention());
        }*/
    }

}
