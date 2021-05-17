package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.server.nethandler.IHandler;

public class ChatHandler implements IHandler<ClientChatPacket>
{
    @Override
    public boolean handle(ClientChatPacket packet)
    {
        String message = packet.getMessage();
        if (message.startsWith("@Server"))
        {
            PingBypass.logger.info("Received Command: " + message);
            String command = message.substring(7);
            PingBypass.commandManager.onMessage(command);
            return false;
        }

        return true;
    }

}
