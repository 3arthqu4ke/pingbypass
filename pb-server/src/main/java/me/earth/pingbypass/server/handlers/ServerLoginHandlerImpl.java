package me.earth.pingbypass.server.handlers;

import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.commons.protocol.login.ServerLoginHandler;
import me.earth.pingbypass.commons.protocol.login.c2s.C2SStartLoginPacket;
import me.earth.pingbypass.commons.protocol.login.c2s.C2SValidateServerPacket;
import me.earth.pingbypass.commons.protocol.login.s2c.S2CStartLogin;
import me.earth.pingbypass.commons.protocol.login.s2c.S2CValidateClientPacket;
import me.earth.pingbypass.security.CipherUtil;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.earth.pingbypass.commons.Constants.RANDOM;

@RequiredArgsConstructor
public class ServerLoginHandlerImpl implements IHandler, ServerLoginHandler {
    private final byte[] challenge = Ints.toByteArray(RANDOM.nextInt());
    private final AtomicBoolean validated = new AtomicBoolean();
    private final int transactionId = RANDOM.nextInt();
    private final PingBypassServer server;
    @Getter
    private final Session session;

    public void sendChallenge() {
        session.send(new S2CValidateClientPacket(transactionId, challenge, server.getId()));
    }

    @Override
    public void processValidateServer(C2SValidateServerPacket packet) {
        /*if (packet.getTransactionId() != transactionId) {
            server.getSessionManager().disconnect(session, Component.literal("Invalid transaction id"));
            return;
        }*/

        Optional<PublicKey> publicKey = server.getSecurityManager().getPublicKey(packet.getClientId());
        if (publicKey.isPresent()) {
            if (CipherUtil.isChallengeValid(challenge, packet.getSolvedChallenge(), publicKey.get())) {
                session.setAdmin(server.getAdminService().isAdmin(packet.getClientId()));
                validated.set(true);
                PrivateKey privateKey = server.getSecurityManager().getPrivateKey();
                byte[] encrypted = CipherUtil.encryptUsingKey(privateKey, packet.getChallenge());
                session.send(new S2CStartLogin(transactionId, encrypted));
            } else {
                server.getSessionManager().disconnect(session,
                        Component.literal("Client '%s' failed authentication!".formatted(packet.getClientId())));
            }
        } else {
            server.getSessionManager().disconnect(session,
                    Component.literal("No PublicKey registered for '%s'!".formatted(packet.getClientId())));
        }
    }

    @Override
    public void processStartLogin(C2SStartLoginPacket packet) {
        /*if (packet.getTransactionId() != transactionId) {
            server.getSessionManager().disconnect(session, Component.literal("Invalid transaction id"));
            return;
        }*/

        if (!validated.get()) {
            server.getSessionManager().disconnect(session, Component.literal("Client has not authenticated yet!"));
            return;
        }


    }

    @Override
    public PacketFlow flow() {
        return null;
    }

    @Override
    public ConnectionProtocol protocol() {
        return null;
    }
}
