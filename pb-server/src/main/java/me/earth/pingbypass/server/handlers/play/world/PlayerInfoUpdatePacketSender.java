package me.earth.pingbypass.server.handlers.play.world;

import me.earth.pingbypass.server.mixins.network.IClientboundPlayerInfoUpdatePacket;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.multiplayer.ClientPacketListener;
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

public class PlayerInfoUpdatePacketSender {
    public void send(Session session, ClientPacketListener connection, Level level) {
        var packet = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(Collections.emptyList());
        List<ClientboundPlayerInfoUpdatePacket.Entry> list = new ArrayList<>(level.players().size());
        for (Player player : level.players()) {
            // make sure we do not send FakePlayers
            if (player instanceof RemotePlayer || player instanceof LocalPlayer) {
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
                }
            }
        }

        ((IClientboundPlayerInfoUpdatePacket) packet).setEntries(list);
        session.send(packet);
    }

}
