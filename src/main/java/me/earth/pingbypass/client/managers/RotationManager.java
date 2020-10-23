package me.earth.pingbypass.client.managers;

import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.earthhack.impl.util.math.RotationUtil;
import me.earth.pingbypass.event.events.CPacketEvent;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;

public class RotationManager extends SubscriberImpl implements Globals
{
    private static final RotationManager INSTANCE = new RotationManager();

    private float last_yaw;
    private float last_pitch;

    private RotationManager()
    {
        this.listeners.add(new EventListener<PacketEvent.Receive<SPacketPlayerPosLook>>(PacketEvent.Receive.class, Integer.MAX_VALUE, SPacketPlayerPosLook.class)
        {
            @Override
            public void invoke(PacketEvent.Receive<SPacketPlayerPosLook> event)
            {
                mc.addScheduledTask(() ->
                {
                    SPacketPlayerPosLook packet = event.getPacket();
                    float yaw = packet.getYaw();
                    float pitch = packet.getPitch();

                    if (packet.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT))
                    {
                        yaw += mc.player.rotationYaw;
                    }

                    if (packet.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT))
                    {
                        pitch += mc.player.rotationPitch;
                    }

                    last_yaw = yaw;
                    last_pitch = pitch;
                });
            }
        });
        this.listeners.add(new EventListener<CPacketEvent.Post<ClientPlayerRotationPacket>>(CPacketEvent.Post.class, ClientPlayerRotationPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Post<ClientPlayerRotationPacket> event)
            {
                readCPacket(event.getPacket());
            }
        });
        this.listeners.add(new EventListener<CPacketEvent.Post<ClientPlayerPositionRotationPacket>>(CPacketEvent.Post.class, ClientPlayerPositionRotationPacket.class)
        {
            @Override
            public void invoke(CPacketEvent.Post<ClientPlayerPositionRotationPacket> event)
            {
                readCPacket(event.getPacket());
            }
        });
    }

    public static RotationManager getInstance()
    {
        return INSTANCE;
    }

    public float getYaw()
    {
        return last_yaw;
    }

    public float getPitch()
    {
        return last_pitch;
    }

    public Vec3d getVec()
    {
        return RotationUtil.getVec3d(last_yaw, last_pitch);
    }

    private void readCPacket(ClientPlayerMovementPacket packetIn)
    {
        last_yaw   = (float) packetIn.getYaw();
        last_pitch = (float) packetIn.getPitch();
    }

}
