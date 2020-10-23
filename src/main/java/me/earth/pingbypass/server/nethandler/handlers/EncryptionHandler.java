package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import me.earth.pingbypass.server.nethandler.IHandler;

public class EncryptionHandler implements IHandler<EncryptionResponsePacket>
{
    @Override
    public boolean handle(EncryptionResponsePacket packet)
    {
        return false;
    }

}
