package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import me.earth.pingbypass.server.nethandler.IHandler;
import me.earth.pingbypass.util.thread.ThreadUtil;

public class PositionHandler implements IHandler<ClientPlayerPositionPacket>
{
    @Override
    public boolean handle(ClientPlayerPositionPacket packet)
    {
        ThreadUtil.runChecked(() ->
        {
            mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
            mc.player.onGround = packet.isOnGround();
        });

        return true;
    }

}
