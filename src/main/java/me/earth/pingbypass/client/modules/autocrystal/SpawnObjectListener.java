package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.math.RayTraceUtil;
import me.earth.earthhack.impl.util.math.RotationUtil;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.minecraft.EntityUtil;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.modules.autocrystal.modes.Rotate;
import me.earth.pingbypass.mixin.mixins.minecraft.network.client.ICPacketUseEntity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SpawnObjectListener extends
        ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSpawnObject>>
{
    protected SpawnObjectListener(AutoCrystal module)
    {
        super(module, PacketEvent.Receive.class, SPacketSpawnObject.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSpawnObject> event)
    {
        if (module.instant.getValue())
        {
            SPacketSpawnObject packet = event.getPacket();
            if (packet.getType() == 51 && mc.player != null)
            {
                BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                if (module.positions.remove(pos) && isValid(pos) && rotationCheck(pos))
                {
                    float damage = DamageUtil.calculate(pos);
                    if (damage <= module.maxSelfB.getValue()
                            && damage < EntityUtil.getHealth(mc.player) + 1.0)
                    {
                        attack(packet);
                    }
                }
            }
        }
    }

    private void attack(SPacketSpawnObject packetIn)
    {
        if (module.breakTimer.passed(module.breakDelay.getValue()))
        {
            //noinspection ConstantConditions
            ICPacketUseEntity useEntity = (ICPacketUseEntity) new CPacketUseEntity();
            useEntity.setAction(CPacketUseEntity.Action.ATTACK);
            useEntity.setEntityId(packetIn.getEntityID());

            mc.player.connection.sendPacket((Packet<?>) useEntity);
            mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));

            module.breakTimer.reset(module.breakDelay.getValue());
        }
    }

    private boolean isValid(BlockPos pos)
    {
        if (mc.player.getDistanceSq(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) > MathUtil.square(module.breakRange.getValue()))
        {
            return false;
        }

        if (mc.player.getDistanceSq(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) > MathUtil.square(module.breakTrace.getValue()))
        {
            return RayTraceUtil.canBeSeen(new Vec3d(pos.getX() + 0.5, pos.getY() + 1.700000047683716, pos.getZ() + 0.5), mc.player);
        }

        return true;
    }

    private boolean rotationCheck(BlockPos pos)
    {
        return module.rotate.getValue().noRotate(Rotate.Break) || RotationUtil.isLegit(pos);
    }

}
