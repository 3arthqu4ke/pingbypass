package me.earth.pingbypass.util.server;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.text.TextComponentString;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerUtil implements Globals
{
    public static int findEmptyPort()
    {
        int port = 25560;

        try (ServerSocket socket = new ServerSocket(0))
        {
            port = socket.getLocalPort();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }

        return port;
    }

    public static void disconnectFromMC()
    {
        NetHandlerPlayClient connection = mc.getConnection();
        if(connection != null)
        {
            connection.getNetworkManager().closeChannel(new TextComponentString("Quitting"));
        }
    }

}
