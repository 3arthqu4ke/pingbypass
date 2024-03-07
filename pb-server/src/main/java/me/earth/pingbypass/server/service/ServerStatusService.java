package me.earth.pingbypass.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.util.PingUtil;
import me.earth.pingbypass.server.session.SessionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import static me.earth.pingbypass.api.Constants.MC;
import static me.earth.pingbypass.server.ServerConstants.MAX_PLAYERS;
import static net.minecraft.ChatFormatting.*;
import static net.minecraft.network.chat.Component.literal;

@Slf4j
@RequiredArgsConstructor
public class ServerStatusService {
    // TODO: make resource-packable instead?
    private static final @Nullable ServerStatus.Favicon FAVICON;

    private final SessionManager sessionManager;
    private final QueueService queueService;
    private final Minecraft mc;

    public static ChatFormatting getPingColor(int ping) {
        return ping <= 0
                ? WHITE
                : ping <= 25
                ? GREEN
                : ping <= 50
                ? YELLOW
                : ping <= 100
                ? GOLD
                : RED;
    }

    public ServerStatus getServerStatus() {
        return new ServerStatus(getMotd(),
                                Optional.of(new ServerStatus.Players(MAX_PLAYERS, sessionManager.getPlayers(), Collections.emptyList())), // TODO: don't return empty List?
                                Optional.of(new ServerStatus.Version(MC, Constants.MC_PROTOCOL)),
                                Optional.ofNullable(FAVICON),
                                false);
    }

    public LegacyServerStatus getLegacyStatus() {
        return new LegacyServerStatus(MC, getMotd().getString(), sessionManager.getPlayers(), MAX_PLAYERS);
    }

    public Component getMotd() {
        if (mc.isSingleplayer()) {
            return literal("SinglePlayer")
                    .withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        ServerData data = mc.getCurrentServer();
        if (data == null || mc.level == null) {
            return literal("Not connected")
                    .withStyle(ChatFormatting.RED);
        }

        int ping = PingUtil.getPing(mc);
        int pos = queueService.getPosition();
        if (queueService.isOn2b2t() && pos != -1) {
            return literal("2b2t.org")
                    .withStyle(GRAY)
                    .append(literal(", ")
                            .withStyle(GRAY))
                    .append(literal("Queue:")
                            .withStyle(GOLD))
                    .append(literal(String.valueOf(pos))
                            .withStyle(BOLD, GOLD))
                    .append(literal(", ")
                            .withStyle(GRAY))
                    .append(literal("Ping")
                            .withStyle(WHITE))
                    .append(literal(": ")
                            .withStyle(GRAY))
                    .append(literal(String.valueOf(ping))
                            .withStyle(BOLD, getPingColor(ping)));
        }

        return literal(data.ip)
                .withStyle(GREEN)
                .append(literal(", ")
                        .withStyle(GRAY))
                .append(literal("Ping")
                        .withStyle(WHITE))
                .append(literal(": ")
                        .withStyle(GRAY))
                .append(literal(String.valueOf(ping))
                        .withStyle(BOLD, getPingColor(ping)));
    }

    static {
        ServerStatus.Favicon favicon;
        try (InputStream is = ServerStatusService.class.getClassLoader().getResourceAsStream("assets/pingbypass/favicon.png")) {
            if (is == null) {
                throw new IOException("Failed to find pack.png");
            }

            favicon = new ServerStatus.Favicon(is.readAllBytes());
        } catch (IOException e) {
            log.error("Failed to read Favicon", e);
            favicon = null;
        }

        FAVICON = favicon;
    }

}
