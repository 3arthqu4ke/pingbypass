package me.earth.pingbypass.util.server;

import com.github.steveice10.packetlib.Server;

public class ShutDownHook
{

    public static void create(Server server)
    {
        Runtime.getRuntime().addShutdownHook(new Thread(server::close));
    }

}
