package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.server.nethandler.IHandler;

public class LoginHandler implements IHandler<LoginStartPacket>
{
    @Override
    public boolean handle(LoginStartPacket packet)
    {
        PingBypass.client.connect();
        return false;
    }

}
