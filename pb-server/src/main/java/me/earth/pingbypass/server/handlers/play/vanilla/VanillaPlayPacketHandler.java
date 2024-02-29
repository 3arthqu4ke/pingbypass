package me.earth.pingbypass.server.handlers.play.vanilla;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.handlers.AbstractCommonPacketListener;
import me.earth.pingbypass.server.handlers.IServerGamePacketListener;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.CommonListenerCookie;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class VanillaPlayPacketHandler extends AbstractCommonPacketListener implements IServerGamePacketListener {
    public VanillaPlayPacketHandler(PingBypassServer server, Session session, CommonListenerCookie cookie) {
        super(server, session, cookie, server.getMinecraft());
    }



    @Override
    public void onPacket(@NotNull Packet<?> packet) {
        LocalPlayer player = mc.player;
        if (player != null) {
            player.connection.send(packet);
        }
    }

    @Override
    public @NotNull Connection getConnection() {
        return super.getConnection();
    }

    @Override
    public void onDisconnect(@NotNull Component component) {
        log.info("Session disconnected {} {}", session, component);
    }

}
