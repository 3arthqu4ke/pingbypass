package me.earth.pingbypass.server.service;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.commons.Constants;
import me.earth.pingbypass.commons.util.PingUtil;
import me.earth.pingbypass.server.session.SessionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

import static me.earth.pingbypass.commons.Constants.MC;
import static me.earth.pingbypass.server.ServerConstants.MAX_PLAYERS;
import static net.minecraft.ChatFormatting.*;
import static net.minecraft.network.chat.Component.literal;

@RequiredArgsConstructor
public class ServerStatusService {
    public static final String FAVICON = """
            data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAABhUlEQVR42u3WMUgCUQDG8bc0OFhk
            UBE1KRFZg9LgIMUFBi2JSSlUQyY0VBiElFs0OAlNDQWFYUMFQVDU0BBtNoQRjVFLQdAS1NB2bR+v
            4eCuw3wX38dvetyd93cQRcbEbo9mQD5//9wDq+f6dT2YuddoggEM+IOAu/0oCGnyh8nnun4AwmBy
            gNEzGcAAVQLKOxE4yQ/Bc2UFrooTIL/E2+sGVM5moVxMwP15GuRzBjBAlYAfN1R5lt+HAQxgAAMY
            ULMA3WCGf+BMXM8ABjgpgL9CDGAAAxjg/ACr6+9uADsv2uXzwFg0DNX+ghjAALt7OZwEO8/JpkJQ
            yA4AAxigesBCzA8JzQtm7u0LtMNyOgRBrwcYwADVA+TdbMbBzPXTI35YmwuDqNUYwACbS2o+yIz2
            gHxNsLcNCrkIdDS5gAEMcGqAvNhgJxzvTsHqkgatzW4Qqo0BDPjF3K46KOU0eLxchK+PLUiNB4AB
            DPgPAS2NLngoJeE0PwxPF/OwvR4HBjBAkYBveo6/J887s58AAAAASUVORK5CYII=""";

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
                                Optional.of(new ServerStatus.Favicon(FAVICON.getBytes(StandardCharsets.UTF_8))),
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

        int ping = PingUtil.getPing();
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

}
