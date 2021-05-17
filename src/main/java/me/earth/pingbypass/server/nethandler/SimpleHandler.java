package me.earth.pingbypass.server.nethandler;

import com.github.steveice10.packetlib.packet.Packet;

public class SimpleHandler implements IHandler<Packet>
{
    @Override
    public boolean handle(Packet packet)
    {
        return false;
    }

}
