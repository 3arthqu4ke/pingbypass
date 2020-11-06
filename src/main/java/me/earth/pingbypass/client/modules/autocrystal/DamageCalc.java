package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.helpers.Wrapper;
import me.earth.earthhack.impl.util.math.PositionUtil;
import me.earth.earthhack.impl.util.math.RayTraceUtil;
import me.earth.earthhack.impl.util.math.RotationUtil;
import me.earth.earthhack.impl.util.minecraft.BlockUtil;
import me.earth.earthhack.impl.util.minecraft.EntityUtil;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.managers.SwitchManager;
import me.earth.pingbypass.client.modules.autocrystal.modes.Rotate;
import me.earth.pingbypass.client.modules.autocrystal.util.BreakData;
import me.earth.pingbypass.client.modules.autocrystal.util.PlaceData;
import me.earth.pingbypass.mixin.mixins.minecraft.network.server.ISPacketSpawnExperienceOrb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DamageCalc extends Wrapper<AutoCrystal> implements Runnable, Globals
{
    private final List<Packet<?>> packets = new CopyOnWriteArrayList<>();
    private final List<EntityPlayer> players;
    private final List<Entity> crystals;
    private final BreakHelper breakHelper;
    private final PlaceHelper placeHelper;

    private BlockPos pos;
    private EntityPlayer target;
    private float[] rotations;
    private boolean attacking;
    private BreakData breakData;

    public DamageCalc(AutoCrystal module, List<EntityPlayer> players, List<Entity> crystals)
    {
        super(module);
        this.players = players;
        this.crystals = crystals;
        this.breakHelper = module.breakHelper;
        this.placeHelper = module.placeHelper;
    }

    @Override
    public void run()
    {
        if (InventoryUtil.isHolding(Items.END_CRYSTAL) && SwitchManager.getInstance().getLastSwitch() >= value.cooldown.getValue())
        {
            int count = explode();
            if (count != 6 && value.place.getValue() && value.placeTimer.passed(value.placeDelay.getValue()) && (count < value.multiPlace.getValue() || value.antiSurr.getValue()))
            {
                PlaceData data = placeHelper.createData(players, crystals);
                if (checkPos(data.getPos(), count))
                {
                    float damage = data.getDamage();
                    if (damage > value.minDamage.getValue() || EntityUtil.getHealth(data.getTarget()) <= value.facePlace.getValue() && damage > 2.0)
                    {
                        target = data.getTarget();
                        place(data.getPos());
                    }
                }
            }
        }
    }

    public float[] getRotations()
    {
        return rotations;
    }

    public List<Packet<?>> getPackets()
    {
        return packets;
    }

    public EntityPlayer getTarget()
    {
        return target;
    }

    private int explode()
    {
        breakData = breakHelper.createData(players, crystals);
        Entity crystal = breakData.getCrystal();
        target = breakData.getTarget();
        if (attack(crystal) && (!value.rotate.getValue().noRotate(Rotate.Place) || !value.multiTask.getValue()))
        {
            return 6;
        }

        return breakData.getCount();
    }

    /**
     * Sends the packets required to break a crystal, or
     * if rotation to it is required, sets this calcs rotations
     * and adds the packets to the queue. In that case this
     * method returns <tt>true</tt>.
     *
     * @param crystal the crystal to break.
     * @return <tt>true</tt> if rotating.
     */
    private boolean attack(Entity crystal)
    {
        if (value.explode.getValue() && crystal != null && value.breakTimer.passed(value.breakDelay.getValue()))
        {
            CPacketUseEntity useEntity = new CPacketUseEntity(crystal);
            CPacketAnimation animation = new CPacketAnimation(EnumHand.MAIN_HAND);
            value.breakTimer.reset(value.breakDelay.getValue());
            attacking = true;

            PingBypass.server.sendToClient(createRenderPacket(BlockPos.ORIGIN, true));

            if (value.rotate.getValue().noRotate(Rotate.Break) || RotationUtil.isLegit(crystal))
            {
                mc.player.connection.sendPacket(useEntity);
                mc.player.connection.sendPacket(animation);
                return !value.multiTask.getValue();
            }
            else
            {
                rotations = RotationUtil.getRotations(crystal);
                packets.add(useEntity);
                packets.add(animation);
                return true;
            }
        }

        return false;
    }

    private void place(BlockPos pos)
    {
        this.pos = pos;
        RayTraceResult result;
        if (!value.rotate.getValue().noRotate(Rotate.Place))
        {
            rotations = RotationUtil.getRotations(pos.up());
            result = RayTraceUtil.getRayTraceResult(rotations[0], rotations[1], value.placeRange.getValue());
        }
        else
        {
            result = new RayTraceResult(new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP);
        }

        CPacketPlayerTryUseItemOnBlock place = new CPacketPlayerTryUseItemOnBlock(
                pos, result.sideHit, getHand(), (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z);
        CPacketAnimation animation = new CPacketAnimation(getHand());

        value.placeTimer.reset(value.placeDelay.getValue());
        value.positions.add(pos.up());

        if ((value.rotate.getValue().noRotate(Rotate.Place) || RotationUtil.isLegit(pos)) && packets.isEmpty())
        {
            mc.player.connection.sendPacket(place);
            mc.player.connection.sendPacket(animation);
        }
        else
        {
            packets.add(place);
            packets.add(animation);
        }

        PingBypass.server.sendToClient(createRenderPacket(pos, false));
    }

    @SuppressWarnings("ConstantConditions")
    private SPacketSpawnExperienceOrb createRenderPacket(BlockPos pos, boolean swingArm)
    {
        ISPacketSpawnExperienceOrb packet = (ISPacketSpawnExperienceOrb) new SPacketSpawnExperienceOrb();
        packet.setEntityId(-1337);
        packet.setExp(swingArm ? -1338 : -1337);
        packet.setX(pos.getX());
        packet.setY(pos.getY());
        packet.setZ(pos.getZ());

        return (SPacketSpawnExperienceOrb) packet;
    }

    /**
     * Check the position a second time, so we
     * dont multiplace with AntiSurround. Just place
     * if there's a crystal on the block or the count
     * is smaller than the multiplace setting.
     *
     * @param pos the place position.
     * @param count amount of damaging crystals.
     * @return <tt>true</tt> if position is fine.
     */
    private boolean checkPos(BlockPos pos, int count)
    {
        boolean rotating = false;
        if (!attacking && value.fallBack.getValue())
        {
            Entity fallBack = breakData.getFallBack();
            if (fallBack != null)
            {
                if (value.antiSurr.getValue())
                {
                    if (pos != null)
                    {
                        for (Entity entity : crystals)
                        {
                            if (entity instanceof EntityEnderCrystal && !entity.isDead)
                            {
                                if (entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos.up()))
                                        || (!value.newerVer.getValue() && entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos.up(2)))))
                                {
                                    BlockPos entityPos = PositionUtil.getPosition(entity);
                                    if (!entityPos.equals(pos.up()))
                                    {
                                        rotating = attack(fallBack) //attack == true ensures that rotations != null
                                                && !value.rotate.getValue().noRotate(Rotate.Place)
                                                && !RotationUtil.isLegit(pos.getX(), pos.getY(), pos.getZ(), rotations[0], rotations[1]);
                                    }
                                }
                            }
                        }
                    }
                }
                else if (pos == null && breakData.getFallBackDamage() <= value.fallbackDmg.getValue())
                {
                    attack(fallBack);
                    return false;
                }
            }
        }

        return pos != null && !rotating && (count < value.multiPlace.getValue() || (value.antiSurr.getValue() && !BlockUtil.canPlaceCrystal(pos, false, value.newerVer.getValue(), crystals)));
    }

    private EnumHand getHand()
    {
        return mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL
                ? EnumHand.OFF_HAND
                : EnumHand.MAIN_HAND;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public boolean isAttacking()
    {
        return attacking;
    }

}
