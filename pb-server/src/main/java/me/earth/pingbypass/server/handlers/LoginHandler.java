package me.earth.pingbypass.server.handlers;

import com.google.common.primitives.Ints;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.CrashReportCategory;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.*;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.util.Crypt;
import net.minecraft.util.CryptException;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A custom {@link net.minecraft.server.network.ServerLoginPacketListenerImpl}.
 */
@Slf4j
@RequiredArgsConstructor
public class LoginHandler implements ServerLoginPacketListener, TickablePacketListener, IHandler {
    private static final Component DISCONNECT_UNEXPECTED_QUERY = Component.translatable("multiplayer.disconnect.unexpected_query_response");
    private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
    private static final int MAX_TICKS_BEFORE_LOGIN = 600;

    private final byte[] challenge = Ints.toByteArray(RandomSource.create().nextInt());
    private final PingBypassServer server;
    @Getter
    private final Session session;

    private volatile State state = State.HELLO;
    private int tick;
    @Nullable
    private String requestedUsername;
    @Nullable
    private GameProfile authenticatedProfile;

    @Override
    public void tick() {
        if (state == State.VERIFYING) {
            verifyLoginAndFinishConnectionSetup(Objects.requireNonNull(authenticatedProfile));
        }

        if (state == State.WAITING_FOR_DUPE_DISCONNECT) {
            finishLoginAndWaitForClient(Objects.requireNonNull(authenticatedProfile));
        }

        if (tick++ == MAX_TICKS_BEFORE_LOGIN) {
            disconnect(Component.translatable("multiplayer.disconnect.slow_login"));
        }
    }

    @Override
    public void onDisconnect(@NotNull Component reason) {
        log.info("{} lost connection: {}", getUserName(), reason.getString());
    }

    @Override
    public void handleHello(ServerboundHelloPacket packet) {
        Validate.validState(state == State.HELLO, "Unexpected hello packet");
        Validate.validState(Player.isValidUsername(packet.name()), "Invalid characters in username");
        // requestedUsername = packet.name();
        requestedUsername = server.getMinecraft().getGameProfile().getName();
        // no need for singleplayer gameprofile
        if (server.getServerConfig().get(ServerConstants.AUTH) && !session.isMemoryConnection()) {
            state = State.KEY;
            session.send(new ClientboundHelloPacket("", ServerConstants.KEY_PAIR.getPublic().getEncoded(), challenge));
        } else {
            startClientVerification(UUIDUtil.createOfflineProfile(requestedUsername));
        }
    }

    // TODO:!
    @Override
    public void handleKey(ServerboundKeyPacket packet) {
        Validate.validState(state == State.KEY, "Unexpected key packet");

        final String serverId;
        try {
            PrivateKey privateKey = ServerConstants.KEY_PAIR.getPrivate();
            if (!packet.isChallengeValid(challenge, privateKey)) {
                throw new IllegalStateException("Protocol error");
            }

            SecretKey secretKey = packet.getSecretKey(privateKey);
            Cipher cipher = Crypt.getCipher(2, secretKey);
            Cipher cipher2 = Crypt.getCipher(1, secretKey);
            serverId = new BigInteger(Crypt.digestData("", ServerConstants.KEY_PAIR.getPublic(), secretKey)).toString(16);
            state = State.AUTHENTICATING;
            session.setEncryptionKey(cipher, cipher2);
        } catch (CryptException e) {
            throw new IllegalStateException("Protocol error", e);
        }
        // TODO: use Executor
        /*Thread thread = new Thread("User Authenticator #" + UNIQUE_THREAD_ID.incrementAndGet()) {
            @Override
            public void run() {
                String string = Objects.requireNonNull(requestedUsername, "Player name not initialized");

                try {
                    ProfileResult profileResult = server.getSessionService().hasJoinedServer(string, string, this.getAddress());
                    if (profileResult != null) {
                        GameProfile gameProfile = profileResult.profile();
                        log.info("UUID of player {} is {}", gameProfile.getName(), gameProfile.getId());
                        startClientVerification(gameProfile);
                    } else if (server.isSingleplayer()) {
                        log.warn("Failed to verify username but will let them in anyway!");
                        startClientVerification(UUIDUtil.createOfflineProfile(string));
                    } else {
                        disconnect(Component.translatable("multiplayer.disconnect.unverified_username"));
                        log.error("Username '{}' tried to join with an invalid session", string);
                    }
                } catch (AuthenticationUnavailableException var4) {
                    if (server.isSingleplayer()) {
                        log.warn("Authentication servers are down but will let them in anyway!");
                        startClientVerification(UUIDUtil.createOfflineProfile(string));
                    } else {
                        disconnect(Component.translatable("multiplayer.disconnect.authservers_down"));
                        log.error("Couldn't verify username because servers are unavailable");
                    }
                }
            }

            @Nullable
            private InetAddress getAddress() {
                SocketAddress socketAddress = connection.getRemoteAddress();
                return server.getPreventProxyConnections() && socketAddress instanceof InetSocketAddress
                        ? ((InetSocketAddress)socketAddress).getAddress()
                        : null;
            }
        };

        thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        thread.start();*/

        startClientVerification(UUIDUtil.createOfflineProfile(Objects.requireNonNull(requestedUsername)));
    }

    @Override
    public void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket packet) {
        disconnect(DISCONNECT_UNEXPECTED_QUERY);
    }

    @Override
    public void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket packet) {
        Validate.validState(state == State.PROTOCOL_SWITCHING, "Unexpected login acknowledgement packet");
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(Objects.requireNonNull(authenticatedProfile));
        session.setCookie(cookie);
        ConfigurationPacketListener configurationPacketListener = new ConfigurationPacketListener(server, session, cookie);
        session.setListener(configurationPacketListener);
        configurationPacketListener.startConfiguration();
        state = State.ACCEPTED;
    }

    @Override
    public void fillListenerSpecificCrashDetails(CrashReportCategory crashReportCategory) {
        crashReportCategory.setDetail("Login phase", () -> state.toString());
    }

    private void disconnect(Component reason) {
        try {
            log.info("Disconnecting {}: {}", getUserName(), reason.getString());
            server.getSessionManager().disconnect(session, reason);
        } catch (Exception e) {
            log.error("Error whilst disconnecting player", e);
        }
    }

    private String getUserName() {
        String string = session.getLoggableAddress(server.getServerConfig().get(ServerConstants.LOG_IPS));
        return requestedUsername != null ? requestedUsername + " (" + string + ")" : string;
    }

    private void startClientVerification(GameProfile authenticatedProfile) {
        this.authenticatedProfile = authenticatedProfile;
        state = State.VERIFYING;
    }

    private void verifyLoginAndFinishConnectionSetup(GameProfile profile) {
        int compression = server.getServerConfig().get(ServerConstants.COMPRESSION);
        if (compression >= 0 && !session.isMemoryConnection()) {
            session.send(new ClientboundLoginCompressionPacket(compression),
                    PacketSendListener.thenRun(() -> session.setupCompression(compression, true)));
        }

        // TODO: here!!!!
        state = State.WAITING_FOR_DUPE_DISCONNECT;
        finishLoginAndWaitForClient(profile);
    }

    private void finishLoginAndWaitForClient(GameProfile profile) {
        state = State.PROTOCOL_SWITCHING;
        session.send(new ClientboundGameProfilePacket(server.getMinecraft().getGameProfile()));
    }

    private enum State {
        HELLO,
        KEY,
        AUTHENTICATING,
        // NEGOTIATING, (not used)
        VERIFYING,
        WAITING_FOR_DUPE_DISCONNECT,
        PROTOCOL_SWITCHING,
        ACCEPTED
    }

}
