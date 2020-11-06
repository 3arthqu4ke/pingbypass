package me.earth.pingbypass.client.modules.autocrystal;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import me.earth.earthhack.api.util.Globals;
import me.earth.pingbypass.client.modules.autocrystal.modes.Rotate;
import me.earth.pingbypass.event.ClientPlayerListener;
import me.earth.pingbypass.event.events.CPacketEvent;

/**
 * By using the point when a CPacketPlayer arrives from the client
 * and the point after we sent it to the server, we can simulate
 * onUpdateWalkingPlayer and spoof the rotations. If we want to rotate
 * the client has to ensure that we always send a rotation packet.
 */
public class PreListener extends ClientPlayerListener implements Globals
{
    private final AutoCrystal module;

    protected PreListener(AutoCrystal module)
    {
        this.module = module;
    }

    @Override
    protected void onPacket(CPacketEvent.Send<ClientPlayerMovementPacket> event)
    {
        if (module.rotate.getValue() == Rotate.None)
        {
            startThread(event.getPacket());
        }
    }

    @Override
    protected void onPosition(CPacketEvent.Send<ClientPlayerPositionPacket> event)
    {
        if (module.rotate.getValue() == Rotate.None)
        {
            startThread(event.getPacket());
        }
    }

    @Override
    protected void onRotation(CPacketEvent.Send<ClientPlayerRotationPacket> event)
    {
        startThread(event.getPacket());
    }

    @Override
    protected void onPositionRotation(CPacketEvent.Send<ClientPlayerPositionRotationPacket> event)
    {
        startThread(event.getPacket());
    }

    private void startThread(ClientPlayerMovementPacket packet)
    {
        module.onTick(packet);
    }

}
