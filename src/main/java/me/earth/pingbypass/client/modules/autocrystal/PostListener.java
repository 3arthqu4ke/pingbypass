package me.earth.pingbypass.client.modules.autocrystal;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import me.earth.pingbypass.event.ClientPlayerPostListener;
import me.earth.pingbypass.event.events.CPacketEvent;

public class PostListener extends ClientPlayerPostListener
{
    private final AutoCrystal module;

    protected PostListener(AutoCrystal module)
    {
        this.module = module;
    }

    @Override
    protected void onPacket(CPacketEvent.Post<ClientPlayerMovementPacket> event)
    {
        module.end(event.getPacket());
    }

    @Override
    protected void onPosition(CPacketEvent.Post<ClientPlayerPositionPacket> event)
    {
        module.end(event.getPacket());
    }

    @Override
    protected void onRotation(CPacketEvent.Post<ClientPlayerRotationPacket> event)
    {
        module.end(event.getPacket());
    }

    @Override
    protected void onPositionRotation(CPacketEvent.Post<ClientPlayerPositionRotationPacket> event)
    {
        module.end(event.getPacket());
    }

}

