package me.earth.pingbypass.client.managers.safety;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.math.PositionUtil;
import me.earth.earthhack.impl.util.minecraft.BlockUtil;
import me.earth.earthhack.impl.util.minecraft.DamageUtil;
import me.earth.earthhack.impl.util.minecraft.EntityUtil;
import me.earth.earthhack.impl.util.minecraft.HoleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class SafetyRunnable implements Globals, Runnable
{
    private final SafetyManager manager;
    private final List<EntityPlayer> players;
    private final List<Entity> crystals;
    private final boolean newerVersion;
    private final boolean safetyPlayer;
    private final boolean bedCheck;
    private final float maxDamage;

    public SafetyRunnable(SafetyManager manager, List<EntityPlayer> players, List<Entity> crystals, boolean newerVersion, boolean safetyPlayer, boolean bedCheck, float maxDamage)
    {
        this.manager = manager;
        this.players  = players;
        this.crystals = crystals;
        this.newerVersion = newerVersion;
        this.safetyPlayer = safetyPlayer;
        this.bedCheck = bedCheck;
        this.maxDamage = maxDamage;
    }

    @Override
    public void run()
    {
        //search for bad crystals in range.
        for (Entity entity : crystals)
        {
            if (entity instanceof EntityEnderCrystal && !entity.isDead)
            {
                if (safetyPlayer)
                {
                    Vec3d vec = entity.getPositionVector();
                    EntityPlayer closest = EntityUtil.getClosestEnemy(vec, players);
                    if (closest == null || closest.getDistanceSq(vec.x, vec.y, vec.z) > 7.5f)
                    {
                        continue;
                    }
                }

                float damage = DamageUtil.calculate(entity, mc.player);
                if (damage > maxDamage || damage > EntityUtil.getHealth(mc.player) + 1.0)
                {
                    manager.setSafe(false);
                    return;
                }
            }
        }

        //If we are in a hole and no bedcheck is required we can just stop it here and not do the big calc.
        BlockPos position = PositionUtil.getPosition(mc.player);
        if (HoleUtil.isHole(position)[0] && (!newerVersion || !bedCheck))
        {
            manager.setSafe(true);
            return;
        }
        else if ((HoleUtil.is2x1(position) || HoleUtil.is2x2(position)) && !bedCheck)
        {
            manager.setSafe(true);
            return;
        }

        //search for bad positions in range.
        for (BlockPos pos : BlockUtil.getSphere(6.0f))
        {
            if (bedCheck && BlockUtil.canPlaceBed(pos, newerVersion) || BlockUtil.canPlaceCrystal(pos, false, newerVersion, crystals))
            {
                if (safetyPlayer)
                {
                    EntityPlayer closest = EntityUtil.getClosestEnemy(pos, players);
                    if (closest == null || closest.getDistanceSq(pos) > 49)
                    {
                        continue;
                    }
                }

                float damage = DamageUtil.calculate(pos, mc.player);
                if (damage > maxDamage || damage > EntityUtil.getHealth(mc.player) + 1.0)
                {
                    manager.setSafe(false);
                    return;
                }
            }
        }

        manager.setSafe(true);
    }

}
