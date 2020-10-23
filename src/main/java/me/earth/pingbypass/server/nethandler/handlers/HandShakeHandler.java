package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.server.nethandler.IHandler;

public class HandShakeHandler implements IHandler<HandshakePacket>
{
    @Override
    public boolean handle(HandshakePacket packet)
    {
        PingBypass.client.prepareConnection(packet);
        return false;
    }

}
