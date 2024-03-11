package me.earth.pingbypass.server.handlers;

import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.util.exceptions.NullabilityUtil;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.*;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.util.VisibleForDebug;
import org.jetbrains.annotations.Nullable;

/**
 * A custom {@link net.minecraft.server.network.ServerCommonPacketListenerImpl}.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommonPacketListener implements IHandler, TickablePacketListener, ServerCommonPacketListener {
    private static final Component TIMEOUT_DISCONNECTION_MESSAGE = Component.translatable("disconnect.timeout");
    public static final int LATENCY_CHECK_INTERVAL = 15000;

    protected final PingBypassServer server;
    @Getter
    protected final Session session;
    protected final CommonListenerCookie cookie;
    protected final Minecraft mc;

    private volatile boolean suspendFlushingOnServerThread = false;
    private long keepAliveTime = Util.getMillis();
    private int latency = 0;

    private boolean keepAlivePending;
    private long keepAliveChallenge;

    protected GameProfile playerProfile() {
        return cookie.gameProfile();
    }

    @Override
    public void tick() {
        this.keepConnectionAlive();
    }

    @Override
    public void handleKeepAlive(ServerboundKeepAlivePacket packet) {
        if (keepAlivePending && packet.getId() == keepAliveChallenge) {
            int difference = (int)(Util.getMillis() - keepAliveTime);
            latency = (latency * 3 + difference) / 4;
            keepAlivePending = false;
        } else {
            disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
        }
    }

    @Override
    public void handlePong(ServerboundPongPacket packet) {
        // do not proxy pongs
    }

    @Override
    public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        // TODO: custom protocol
    }

    protected void keepConnectionAlive() {
        long l = Util.getMillis();
        if (l - keepAliveTime >= LATENCY_CHECK_INTERVAL) {
            if (keepAlivePending) {
                disconnect(TIMEOUT_DISCONNECTION_MESSAGE);
            } else {
                keepAlivePending = true;
                keepAliveTime = l;
                keepAliveChallenge = l;
                log.info("Sending ClientboundKeepAlivePacket");
                send(new ClientboundKeepAlivePacket(keepAliveChallenge));
            }
        }
    }

    public void suspendFlushing() {
        suspendFlushingOnServerThread = true;
    }

    public void resumeFlushing() {
        suspendFlushingOnServerThread = false;
        session.flushChannel();
    }

    public void send(Packet<?> packet) {
        send(packet, null);
    }

    public void send(Packet<?> packet, @Nullable PacketSendListener listener) {
        boolean flush = !suspendFlushingOnServerThread || !mc.isSameThread();

        try {
            session.send(packet, listener, flush);
        } catch (Throwable e) {
            CrashReport crashReport = CrashReport.forThrowable(e, "Sending packet");
            CrashReportCategory crashReportCategory = crashReport.addCategory("Packet being sent");
            crashReportCategory.setDetail("Packet class", () -> packet.getClass().getCanonicalName());
            throw new ReportedException(crashReport);
        }
    }

    public void disconnect(Component reason) {
        server.getSessionManager().disconnect(session, reason);
    }

    @VisibleForDebug
    public GameProfile getOwner() {
        return playerProfile();
    }

    public int latency() {
        return latency;
    }

    protected CommonListenerCookie createCookie(ClientInformation clientInformation) {
        return new CommonListenerCookie(playerProfile(), latency, clientInformation);
    }

    // TODO: when we schedule we still need to handle the packet!
    protected void scheduleSafely(NullabilityUtil.PlayerLevelAndGameModeConsumer action) {
        mc.submit(() -> NullabilityUtil.safe(mc, action));
    }

}
