package me.earth.pingbypass.server.handlers.play;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.handlers.AbstractCommonPacketListener;
import me.earth.pingbypass.server.handlers.IServerGamePacketListener;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.server.network.CommonListenerCookie;
import org.jetbrains.annotations.NotNull;

@Slf4j
public abstract class AbstractPlayPacketHandler extends AbstractCommonPacketListener implements IServerGamePacketListener {
    public AbstractPlayPacketHandler(PingBypassServer server, Session session, CommonListenerCookie cookie) {
        super(server, session, cookie, server.getMinecraft());
    }

    @Override
    public void handleKeepAlive(@NotNull ServerboundKeepAlivePacket packet) {
        super.handleKeepAlive(packet);
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
