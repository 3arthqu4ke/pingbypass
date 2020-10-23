package me.earth.pingbypass.server.nethandler.handlers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import me.earth.pingbypass.server.nethandler.IHandler;
import me.earth.pingbypass.util.thread.ThreadUtil;
import net.minecraft.util.math.MathHelper;

public class RotationHandler implements IHandler<ClientPlayerRotationPacket>
{
    @Override
    public boolean handle(ClientPlayerRotationPacket packet)
    {
        final float yaw   = (float) packet.getYaw();
        final float pitch = (float) MathHelper.clamp(packet.getPitch(), -90.0F, 90.0F);

        ThreadUtil.runChecked(() ->
        {
            mc.player.rotationYaw   = yaw;
            mc.player.rotationPitch = pitch;
        });

        return true;
    }

}
