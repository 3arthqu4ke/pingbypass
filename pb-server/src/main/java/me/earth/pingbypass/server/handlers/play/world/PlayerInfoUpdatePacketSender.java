package me.earth.pingbypass.server.handlers.play.world;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.util.PingUtil;
import me.earth.pingbypass.server.handlers.play.GameProfileTranslation;
import me.earth.pingbypass.server.mixins.network.IClientboundPlayerInfoUpdatePacket;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class PlayerInfoUpdatePacketSender {
    public void send(Session session, GameProfileTranslation translation, ClientPacketListener connection, LocalPlayer localPlayer, MultiPlayerGameMode gameMode, Level level) {
        boolean sentToLocalPlayer = false;
        var packet = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.emptyList());
        List<ClientboundPlayerInfoUpdatePacket.Entry> list = new ArrayList<>(level.players().size());
        for (Player player : level.players()) {
            boolean isLocalPlayer = player instanceof LocalPlayer;
            // make sure we do not send FakePlayers
            if (isLocalPlayer || player instanceof RemotePlayer) {
                PlayerInfo info = connection.getPlayerInfo(player.getUUID());
                if (info != null) {
                    boolean isListed = connection.getListedOnlinePlayers().contains(info);
                    RemoteChatSession chatSession = info.getChatSession();
                    list.add(
                            new ClientboundPlayerInfoUpdatePacket.Entry(
                                    info.getProfile().getId(),
                                    info.getProfile(),
                                    isListed,
                                    info.getLatency(),
                                    info.getGameMode(),
                                    info.getTabListDisplayName(),
                                    chatSession == null ? null : chatSession.asData()
                            )
                    );

                    if (isLocalPlayer) {
                        sentToLocalPlayer = true;
                    }
                }
            }
        }

        if (!sentToLocalPlayer) {
            PlayerInfo playerInfo = localPlayer.connection.getPlayerInfo(localPlayer.getUUID());
            list.add(
                new ClientboundPlayerInfoUpdatePacket.Entry(
                    playerInfo == null ? localPlayer.getUUID() : playerInfo.getProfile().getId(),
                    playerInfo == null ? localPlayer.getGameProfile() : playerInfo.getProfile(),
                    playerInfo != null && localPlayer.connection.getListedOnlinePlayers().contains(playerInfo),
                    PingUtil.getPing(session.getServer().getMinecraft()),
                    playerInfo == null ? gameMode.getPlayerMode() : playerInfo.getGameMode(),
                    playerInfo == null ? null : playerInfo.getTabListDisplayName(),
                    null
                )
            );
        }

        ((IClientboundPlayerInfoUpdatePacket) packet).setEntries(list);
        session.send(translation.translate(session, packet));
    }

}
