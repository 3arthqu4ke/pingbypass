package me.earth.pingbypass.client.modules.autototem;

import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.pingbypass.event.events.CPacketEvent;

public class ClickWindowListener extends ModuleListener<AutoTotem, CPacketEvent.Send<ClientWindowActionPacket>>
{
    protected ClickWindowListener(AutoTotem module)
    {
        super(module, CPacketEvent.Send.class, ClientWindowActionPacket.class);
    }

    @Override
    public void invoke(CPacketEvent.Send<ClientWindowActionPacket> event)
    {
        //TODO: idk
    }

}
