package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.helpers.Wrapper;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.math.RayTraceUtil;
import me.earth.earthhack.impl.util.minecraft.BlockUtil;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.minecraft.EntityUtil;
import me.earth.pingbypass.client.managers.FriendManager;
import me.earth.pingbypass.client.modules.autocrystal.modes.Target;
import me.earth.pingbypass.client.modules.autocrystal.util.PlaceData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class PlaceHelper extends Wrapper<AutoCrystal> implements Globals
{
    public PlaceHelper(AutoCrystal module)
    {
        super(module);
    }

    public PlaceData createData(List<EntityPlayer> players, List<Entity> crystals)
    {
        PlaceData data = new PlaceData(value.target.getValue().getTarget(players), crystals);
        if (data.getTarget() != null || value.target.getValue() == Target.Damage)
        {
            evaluate(data, players);
        }

        return data;
    }

    private void evaluate(PlaceData data, List<EntityPlayer> players)
    {
        for (BlockPos pos : BlockUtil.getSphere(value.placeRange.getValue()))
        {
            if (isValid(data, pos))
            {
                float self = value.suicide.getValue() ? -1 : DamageUtil.calculate(pos);
                if (self < EntityUtil.getHealth(mc.player) - 1.0f)
                {
                    if (self > value.maxSelfP.getValue() && !value.override.getValue() || calcFriends(pos, players))
                    {
                        continue;
                    }

                    if (value.target.getValue() == Target.Damage)
                    {
                        for (EntityPlayer player : players)
                        {
                            calc(pos, data, player, self);
                        }
                    }
                    else
                    {
                        calc(pos, data, data.getTarget(), self);
                    }
                }
            }
        }
    }

    private void calc(BlockPos pos, PlaceData data, EntityPlayer player, float self)
    {
        if (!FriendManager.getInstance().isFriend(player))
        {
            float damage = DamageUtil.calculate(pos, player);
            if (self <= value.maxSelfP.getValue() || value.override.getValue() && damage > EntityUtil.getHealth(player) + 1.0)
            {
                if (damage > data.getDamage() || damage >= data.getDamage() && data.getSelfDamage() > self)
                {
                    data.setDamage(damage);
                    data.setSelfDamage(self);
                    data.setTarget(player);
                    data.setPos(pos);
                }
            }
        }
    }

    private boolean calcFriends(BlockPos pos, List<EntityPlayer> players)
    {
        if (value.noFriendP.getValue())
        {
            for (EntityPlayer player : players)
            {
                if (isValid(player, pos) && FriendManager.getInstance().isFriend(player))
                {
                    float damage = DamageUtil.calculate(pos, player);
                    if (damage > EntityUtil.getHealth(player) + 1.0)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean isValid(EntityPlayer player, BlockPos pos)
    {
        if (player != null && !EntityUtil.isDead(player) && !player.equals(mc.player))
        {
            return player.getDistanceSq(pos) <= 144;
        }

        return false;
    }

    private boolean isValid(PlaceData data, BlockPos pos)
    {
        if (BlockUtil.canPlaceCrystal(pos, value.antiSurr.getValue(), value.newerVer.getValue(), getEntityList(data)))
        {
            if (mc.player.getDistanceSq(pos) > MathUtil.square(value.placeTrace.getValue()))
            {
                if (!RayTraceUtil.raytracePlaceCheck(mc.player, pos))
                {
                    return false;
                }
            }

            return combinedTraceCheck(pos);
        }

        return false;
    }

    private boolean combinedTraceCheck(BlockPos pos)
    {
        if (mc.player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5) <= MathUtil.square(value.pbTrace.getValue()))
        {
            return true;
        }

        return RayTraceUtil.canBeSeen(new Vec3d(pos.getX() + 0.5, pos.getY() + 2.700000047683716, pos.getZ() + 0.5), mc.player);
    }

    private List<Entity> getEntityList(PlaceData data)
    {
        return value.multiThread.getValue() ? data.getEntities() : null;
    }

}
