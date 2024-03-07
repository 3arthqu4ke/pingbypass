package me.earth.pingbypass.server.handlers.play;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.mixins.entity.IAbstractClientPlayer;
import me.earth.pingbypass.api.protocol.s2c.S2CGameProfilePacket;
import me.earth.pingbypass.server.mixins.network.IClientboundPlayerInfoUpdatePacket;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;

import java.util.ArrayList;
import java.util.Collections;

/**
 * After the login process the GameProfile sent by the server will have been set immutably on the client,
 * e.g. in a {@link CommonListenerCookie}.
 * So we could land in a situation where we are playing with a different GameProfile on the PingBypassServer than on the Client.
 * This is problematic because some stuff, like {@link AbstractClientPlayer#isSpectator()} depends on it.
 * We can send a {@link S2CGameProfilePacket} to the client, or translate all packets containing our GameProfile to contain the client GameProfile instead.
 */
@Slf4j
public class GameProfileTranslation extends SubscriberImpl {
    public GameProfileTranslation(Session session) {
        listen(new Listener<PipelineEvent<ClientboundPlayerInfoUpdatePacket>>() {
            @Override
            public void onEvent(PipelineEvent<ClientboundPlayerInfoUpdatePacket> event) {
                if (event.getSession() != session) {
                    return;
                }

                event.setPacket(translate(session, event.getOriginal()));
            }
        });
    }

    public ClientboundPlayerInfoUpdatePacket translate(Session session, ClientboundPlayerInfoUpdatePacket packet) {
        LocalPlayer player;
        PlayerInfo playerInfo;
        if ((player = session.getServer().getMinecraft().player) == null || (playerInfo = ((IAbstractClientPlayer) player).invokeGetPlayerInfo()) == null) {
            return packet;
        }

        for (ClientboundPlayerInfoUpdatePacket.Entry entry : packet.entries()) {
            if (playerInfo.getProfile().getId().equals(entry.profileId())) {
                return replace(session, packet, playerInfo);
            }
        }

        return packet;
    }

    private ClientboundPlayerInfoUpdatePacket replace(Session session, ClientboundPlayerInfoUpdatePacket original, PlayerInfo playerInfo) {
        var list = new ArrayList<>(original.entries());
        for (ClientboundPlayerInfoUpdatePacket.Entry entry : original.entries()) {
            if (playerInfo.getProfile().getId().equals(entry.profileId())) {
                list.add(new ClientboundPlayerInfoUpdatePacket.Entry(
                        session.getCookie().gameProfile().getId(),
                        entry.profile() == null ? null : session.getCookie().gameProfile(),
                        entry.listed(),
                        entry.latency(),
                        entry.gameMode(),
                        entry.displayName(),
                        entry.chatSession())
                );
            } else {
                list.add(entry);
            }
        }

        var packet = new ClientboundPlayerInfoUpdatePacket(original.actions(), Collections.emptyList());
        ((IClientboundPlayerInfoUpdatePacket) packet).setEntries(list);
        return packet;
    }

}
