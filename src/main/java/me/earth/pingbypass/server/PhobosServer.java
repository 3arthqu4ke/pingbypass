package me.earth.pingbypass.server;

import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.text.TextColor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.modules.servermodule.ServerModule;
import me.earth.pingbypass.server.managers.SPacketManager;
import me.earth.pingbypass.server.managers.ServerManager;
import me.earth.pingbypass.server.managers.SessionManager;
import me.earth.pingbypass.util.server.ServerUtil;
import me.earth.pingbypass.util.server.ShutDownHook;
import me.earth.pingbypass.util.wrappers.SPacketWrapper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;

import static com.github.steveice10.mc.protocol.MinecraftConstants.VERIFY_USERS_KEY;

public class PhobosServer implements Globals
{
    private final SessionManager sessionManager;
    private Server server;

    public PhobosServer(String host, int port)
    {
        sessionManager = new SessionManager();
        Bus.EVENT_BUS.subscribe(new SPacketManager(this));
        createServer(host, port);
    }

    public void sendToClient(String message)
    {
        SPacketChat sPacketChat = new SPacketChat(new TextComponentString(TextColor.RED + "<PingBypass> " + TextColor.RESET + message));
        sendToClient(sPacketChat);
    }

    public void sendToClient(Packet<?> packet)
    {
        SPacketWrapper wrapper = new SPacketWrapper(packet);
        sendToClient(wrapper);
    }

    public void sendToClient(com.github.steveice10.packetlib.packet.Packet packet)
    {
        sessionManager.sendPacket(packet);
    }

    public void requestSession(Session session)
    {
        if (sessionManager.requestSession(session))
        {
            session.addListener(PingBypass.client.getPacketManager());
        }
    }

    public void removeSession(Session session)
    {
        if (sessionManager.removeSession(session))
        {
            ServerUtil.disconnectFromMC();
        }
    }

    public boolean isConnected()
    {
        return sessionManager.isConnected();
    }

    public int getPort()
    {
        return server.getPort();
    }

    public void close()
    {
        server.close();
    }

    private void createServer(String host, int port)
    {
        server = new Server(host, port, MinecraftProtocol.class, new TcpSessionFactory());
        server.setGlobalFlag(VERIFY_USERS_KEY, false);
        server.addListener(new ServerManager(this));
        ShutDownHook.create(server);
        bind();
    }

    private void bind()
    {
        try
        {
            server.bind(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            createServer(ServerModule.getInstance().getIP(), ServerUtil.findEmptyPort());
            server.bind(true);
        }
    }

}
