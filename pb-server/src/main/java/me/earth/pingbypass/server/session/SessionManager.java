package me.earth.pingbypass.server.session;

import lombok.Getter;
import lombok.Synchronized;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

@Slf4j
@SuppressWarnings({"unchecked", "RedundantSuppression"}) // Delegate
public class SessionManager implements Iterable<Session> {
    @Delegate(types = Iterable.class)
    private final List<Session> sessions = new CopyOnWriteArrayList<>();
    @Getter
    private volatile Session primarySession;

    @Synchronized
    public boolean requestPrimarySession(Session session) {
        if (primarySession != null && session.isAdmin() && !primarySession.isAdmin()) {
            this.disconnect(primarySession, Component.literal("An Admin connected!"));
        }

        if (primarySession == null) {
            primarySession = session;
            session.setPrimarySession(true);
            addSession(session);
            return true;
        }

        return false;
    }

    public void sendToPrimarySession(Packet<?> packet) {
        sendToPrimarySession(() -> packet);
    }

    public void sendToPrimarySession(Supplier<Packet<?>> packet) {
        Session primary = primarySession;
        if (primary != null) {
            primary.send(packet.get());
        }
    }

    public void sendPacket(Packet<?> packet) {
        sessions.forEach(session -> {
            if (session.isCompleted()) {
                session.send(packet);
            }
        });
    }

    public void tick() {
        this.sessions.forEach(session -> {
            if (session.isConnecting()) {
                return;
            }

            if (session.isConnected()) {
                try {
                    session.tick();
                } catch (Exception e) {
                    if (session.isMemoryConnection()) {
                        throw new ReportedException(CrashReport.forThrowable(e, "Ticking memory connection"));
                    }

                    log.warn("Failed to handle packet for {} {}", session.getRemoteAddress(), session.getId(), e);
                    disconnect(session, Component.literal("PingBypass server error!"));
                }
            } else {
                remove(session);
            }
        });
    }

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void disconnect(Session session, Component reason) {
        log.info("Disconnecting %s : %s".formatted(session, reason.getString()));

        try {
            switch (session.getConnectionProtocol()) {
                case PLAY -> session.send(new ClientboundDisconnectPacket(reason), afterwards(session, reason));
                case STATUS -> session.send(new ClientboundLoginDisconnectPacket(reason), afterwards(session, reason));
                default -> {}
            }
        } finally {
            remove(session);
        }
    }

    public int getPlayers() {
        return primarySession == null ? 0 : 1;
    }

    @Synchronized
    private void remove(Session session) {
        session.setReadOnly();
        sessions.remove(session);
        if (session.equals(primarySession)) {
            primarySession = null;
        }
    }

    private PacketSendListener afterwards(Session session, Component reason) {
        return PacketSendListener.thenRun(() -> {
            session.disconnect(reason);
            session.handleDisconnection();
        });
    }

}
