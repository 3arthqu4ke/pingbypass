package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import me.earth.pingbypass.server.nethandler.IHandler;
import me.earth.pingbypass.util.thread.ThreadUtil;

public class CloseWindowHandler implements IHandler<ClientCloseWindowPacket>
{
    @Override
    public boolean handle(ClientCloseWindowPacket packet)
    {
        ThreadUtil.addCheckedTask(() -> mc.player.closeScreen());
        return true;
    }

}
