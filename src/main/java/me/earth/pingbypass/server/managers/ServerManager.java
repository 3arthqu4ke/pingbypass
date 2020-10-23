package me.earth.pingbypass.server.managers;

import com.github.steveice10.packetlib.event.server.ServerBoundEvent;
import com.github.steveice10.packetlib.event.server.ServerClosedEvent;
import com.github.steveice10.packetlib.event.server.ServerClosingEvent;
import com.github.steveice10.packetlib.event.server.ServerListener;
import com.github.steveice10.packetlib.event.server.SessionAddedEvent;
import com.github.steveice10.packetlib.event.server.SessionRemovedEvent;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.server.PhobosServer;

public class ServerManager implements ServerListener
{
    private final PhobosServer server;

    public ServerManager(PhobosServer server)
    {
        this.server = server;
    }

    @Override
    public void serverBound(ServerBoundEvent event)
    {
        PingBypass.logger.info("Server bound to port: " + event.getServer().getPort() + ".");
    }

    @Override
    public void serverClosing(ServerClosingEvent event)
    {
        PingBypass.logger.info("Server closing...");
    }

    @Override
    public void serverClosed(ServerClosedEvent event)
    {
        PingBypass.logger.info("Server closed.");
    }

    @Override
    public void sessionAdded(SessionAddedEvent event)
    {
        server.requestSession(event.getSession());
    }

    @Override
    public void sessionRemoved(SessionRemovedEvent event)
    {
        server.removeSession(event.getSession());
    }

}
