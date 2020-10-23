package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.mixin.mixins.minecraft.gui.IContainer;
import me.earth.pingbypass.server.nethandler.IHandler;
import net.minecraft.client.network.NetHandlerPlayClient;

public class KeepAliveHandler implements IHandler<ClientKeepAlivePacket>
{
    @Override
    public boolean handle(ClientKeepAlivePacket packet)
    {
        if (packet.getPingId() <= -1337)
        {
            NetHandlerPlayClient connection = mc.getConnection();
            if (connection != null)
            {
                int ping = connection.getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
                ServerKeepAlivePacket packetOut = new ServerKeepAlivePacket(ping <= 0 ? 1 : ping == 100 ? 101 : ping); //prevents us from hitting ids the library sends
                PingBypass.server.sendToClient(packetOut);
            }
        }

        return false;
    }

}
