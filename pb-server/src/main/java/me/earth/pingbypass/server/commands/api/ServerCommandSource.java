package me.earth.pingbypass.server.commands.api;

import lombok.Getter;
import me.earth.pingbypass.api.command.DelegatingCommandSource;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.session.Session;

@Getter
public class ServerCommandSource extends DelegatingCommandSource {
    private final PingBypassServer server;
    private final Session session;

    public ServerCommandSource(PingBypassServer pingBypass, Session session) {
        super(pingBypass.getMinecraft(), pingBypass);
        this.server = pingBypass;
        this.session = session;
    }

}
