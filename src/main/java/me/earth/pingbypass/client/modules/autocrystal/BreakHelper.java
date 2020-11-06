package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.helpers.Wrapper;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.minecraft.EntityUtil;
import me.earth.pingbypass.client.managers.FriendManager;
import me.earth.pingbypass.client.modules.autocrystal.util.BreakData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class BreakHelper extends Wrapper<AutoCrystal> implements Globals
{
    public BreakHelper(AutoCrystal module)
    {
        super(module);
    }

    public BreakData createData(List<EntityPlayer> players, List<Entity> crystals)
    {
        BreakData data = new BreakData();
        for (Entity crystal : crystals)
        {
            if (isValid(crystal))
            {
                float self   = value.suicide.getValue() ? -1 : DamageUtil.calculate(crystal);
                float health = EntityUtil.getHealth(mc.player);
                if (self < health + 1.0 || value.suicide.getValue())
                {
                    if (evaluate(data, crystal, players, self))
                    {
                        data.setFallBack(crystal, self);
                    }
                }
            }
        }

        return data;
    }

    private boolean evaluate(BreakData data, Entity crystal, List<EntityPlayer> players, float self)
    {
        boolean validFallBack = self <= value.fallbackDmg.getValue();

        boolean count = false;
        Entity previous = data.getCrystal();
        float previousD = data.getDamage();
        for (EntityPlayer player : players)
        {
            if (isValid(player, crystal))
            {
                float damage = DamageUtil.calculate(crystal, player);
                boolean friend = FriendManager.getInstance().isFriend(player);
                if (value.noFriendP.getValue() && friend)
                {
                    if (damage > EntityUtil.getHealth(player) + 1.0f)
                    {
                        count = false;
                        data.setCrystal(previous);
                        data.setDamage(previousD);
                        validFallBack = false;
                        break;
                    }
                }
                else if (!friend && counts(player, self, damage))
                {
                    if (!count)
                    {
                        count = damage > value.minDamage.getValue() || value.countMin.getValue();
                    }

                    if (damage > data.getDamage())
                    {
                        data.setCrystal(crystal);
                        data.setDamage(damage);
                        data.setTarget(player);
                    }
                }
            }
        }

        if (count)
        {
            data.increment();
        }

        return validFallBack;
    }

    private boolean isValid(Entity crystal)
    {
        if (crystal instanceof EntityEnderCrystal && !crystal.isDead)
        {
            double distance = mc.player.getDistanceSq(crystal);
            if (distance > MathUtil.square(value.breakRange.getValue()))
            {
                return false;
            }

            if (distance > MathUtil.square(value.breakTrace.getValue()))
            {
                return mc.player.canEntityBeSeen(crystal);
            }

            return true;
        }

        return false;
    }

    private boolean isValid(EntityPlayer player, Entity crystal)
    {
        if (player != null && !EntityUtil.isDead(player) && !player.equals(mc.player))
        {
            return player.getDistanceSq(crystal) <= 144;
        }

        return false;
    }

    private boolean counts(EntityPlayer player, float self, float damage)
    {
        if (self > value.maxSelfB.getValue())
        {
            float otherH = EntityUtil.getHealth(player);

            if (value.override.getValue() && damage > otherH + 1.0f)
            {
                return true;
            }
            else if (value.suicide.getValue())
            {
                return damage > value.minDamage.getValue()
                        || otherH < value.facePlace.getValue() && damage > value.minFP.getValue();
            }

            return false;
        }

        return damage > self;
    }

}
