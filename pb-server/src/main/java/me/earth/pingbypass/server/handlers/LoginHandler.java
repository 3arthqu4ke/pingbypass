package me.earth.pingbypass.server.handlers;

import com.google.common.primitives.Ints;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.*;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.util.Objects;
import java.util.UUID;

import static net.minecraft.network.chat.Component.literal;

/**
 * A custom {@link net.minecraft.server.network.ServerLoginPacketListenerImpl}.
 */
@Slf4j
@RequiredArgsConstructor
public class LoginHandler implements ServerLoginPacketListener, TickablePacketListener, IHandler {
    private static final RandomSource RANDOM = RandomSource.create();
    private static final int MAX_TICKS_BEFORE_LOGIN = 600;
    private final byte[] challenge = Ints.toByteArray(RANDOM.nextInt());
    private final PingBypassServer server;
    @Getter
    private final Session session;

    @Nullable
    private GameProfile gameProfile;
    private State state = State.HELLO;
    private int tick;

    public static boolean isValidUsername(String string) {
        return string.chars()
                .filter((i) -> i <= 32 || i >= 127)
                .findAny()
                .isEmpty();
    }

    @Override
    public void handleHello(ServerboundHelloPacket packet) {
        Validate.validState(this.state == State.HELLO, "Unexpected hello packet");
        Validate.validState(isValidUsername(packet.name()), "Invalid characters in username");
        this.session.setUuid(packet.profileId().orElse(null));
        this.session.setUserName(packet.name());
        this.gameProfile = new GameProfile(packet.profileId().orElse(null), packet.name());
        if (server.getServerConfig().get(ServerConstants.ENCRYPT) && !getConnection().isMemoryConnection()) {
            this.state = State.KEY;
            getConnection().send(new ClientboundHelloPacket("", ServerConstants.KEY_PAIR.getPublic().getEncoded(), this.challenge));
        } else {
            this.state = State.READY_TO_ACCEPT;
        }
    }

    @Override
    public void handleKey(ServerboundKeyPacket packet) {
        Validate.validState(this.state == State.KEY, "Unexpected key packet");
        try {
            PrivateKey privateKey = ServerConstants.KEY_PAIR.getPrivate();
            if (!packet.isChallengeValid(this.challenge, privateKey)) {
                throw new IllegalStateException("Protocol error");
            }

            SecretKey secretKey = packet.getSecretKey(privateKey);
            Cipher decrypt = Crypt.getCipher(2, secretKey);
            Cipher encrypt = Crypt.getCipher(1, secretKey);
            this.state = State.READY_TO_ACCEPT;
            getConnection().setEncryptionKey(decrypt, encrypt);
        } catch (CryptException ex) {
            throw new IllegalStateException("Protocol error", ex);
        }
    }

    @Override
    public void tick() {
        if (this.state == State.READY_TO_ACCEPT) {
            this.handleAcceptedLogin();
        }

        if (this.tick++ >= MAX_TICKS_BEFORE_LOGIN) {
            this.disconnect(Component.translatable("multiplayer.disconnect.slow_login"));
        }
    }

    @Override
    public void handleCustomQueryPacket(@NotNull ServerboundCustomQueryPacket serverboundCustomQueryPacket) {
        this.disconnect(Component.translatable("multiplayer.disconnect.unexpected_query_response"));
    }

    public void disconnect(Component component) {
        try {
            log.info("Disconnecting {}: {}", this.getUserName(), component.getString());
            getConnection().send(new ClientboundLoginDisconnectPacket(component));
            getConnection().disconnect(component);
        } catch (Exception exception) {
            log.error("Error whilst disconnecting player", exception);
        }
    }

    public void handleAcceptedLogin() {
        if (!Objects.requireNonNull(this.gameProfile).isComplete()) {
            this.gameProfile = this.createFakeProfile(this.gameProfile);
        }

        if (!server.getSessionManager().requestPrimarySession(session)) {
            server.getSessionManager().disconnect(session, literal("Someone is already connected to this PingBypass!"));
            return;
        }

        this.state = State.ACCEPTED;
        int compression = server.getServerConfig().get(ServerConstants.COMPRESSION);
        if (compression >= 0 && !getConnection().isMemoryConnection()) {
            getConnection().send(new ClientboundLoginCompressionPacket(compression),
                    PacketSendListener.thenRun(() -> getConnection().setupCompression(compression, true)));
        }

        // TODO: sure?
        //connection.send(new ClientboundGameProfilePacket(this.gameProfile));
        if (server.getSecurityManager().isEnabled()) {
            var handler = new ServerLoginHandlerImpl(server, session);
            getConnection().setListener(handler);
            handler.sendChallenge();
        } else {

        }
    }

    public String getUserName() {
        if (this.gameProfile != null) {
            return "%s (%s)".formatted(gameProfile, getConnection().getRemoteAddress());
        } else {
            return String.valueOf(getConnection().getRemoteAddress());
        }
    }

    protected GameProfile createFakeProfile(GameProfile gameProfile) {
        UUID uUID = UUIDUtil.createOfflinePlayerUUID(gameProfile.getName());
        return new GameProfile(uUID, gameProfile.getName());
    }

    private enum State {
        HELLO,
        KEY,
        READY_TO_ACCEPT,
        ACCEPTED
    }

}
