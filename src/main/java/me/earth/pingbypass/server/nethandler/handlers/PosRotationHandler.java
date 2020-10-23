package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import me.earth.pingbypass.server.nethandler.IHandler;
import me.earth.pingbypass.util.thread.ThreadUtil;
import net.minecraft.util.math.MathHelper;

public class PosRotationHandler implements IHandler<ClientPlayerPositionRotationPacket>
{
    @Override
    public boolean handle(ClientPlayerPositionRotationPacket packet)
    {
        final float yaw   = (float) packet.getYaw();
        final float pitch = (float) MathHelper.clamp(packet.getPitch(), -90.0F, 90.0F);

        ThreadUtil.runChecked(() ->
            mc.player.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), yaw, pitch));

        return true;
    }

}
