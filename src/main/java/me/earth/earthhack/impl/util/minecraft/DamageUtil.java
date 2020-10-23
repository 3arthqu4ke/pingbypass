package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.thread.EnchantmentUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class DamageUtil implements Globals
{
    /**
     * Convenience method, calls {@link DamageUtil#calculate(Entity, EntityLivingBase)}
     * for mc.player.
     */
    public static float calculate(Entity crystal)
    {
        return calculate(crystal.posX, crystal.posY, crystal.posZ, mc.player);
    }

    /**
     * Convenience method, calls {@link DamageUtil#calculate(BlockPos, EntityLivingBase)}
     * for mc.player.
     */
    public static float calculate(BlockPos pos)
    {
        return calculate(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player);
    }

    /**
     * Convenience method, calls {@link DamageUtil#calculate(double, double, double, EntityLivingBase)}
     * for for the block pos, x + 0.5, y + 1, z + 0.5;
     */
    public static float calculate(BlockPos pos, EntityLivingBase base)
    {
        return calculate(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, base);
    }

    /**
     * Convenience method, calls {@link DamageUtil#calculate(double, double, double, EntityLivingBase)}
     * for for the entities position.
     */
    public static float calculate(Entity crystal, EntityLivingBase base)
    {
        return calculate(crystal.posX, crystal.posY, crystal.posZ, base);
    }

    /**
     * Calculates the damage an explosion of size 6.0 (Endcrystal) would deal
     * to the targeted EntityLivingBase.
     *
     * @param x the x coordinate of the position.
     * @param y the y coordinate of the position.
     * @param z the z coordinate of the position.
     * @param base the targeted entity.
     * @return damage dealt to the entity.
     */
    public static float calculate(double x, double y, double z, EntityLivingBase base)
    {
        double distance = base.getDistance(x, y, z) / 12.0D;
        if (distance > 1.0D)
        {
            return 0.0F;
        }
        else
        {
            //double density = base.world.getBlockDensity(new Vec3d(x, y, z), base.getEntityBoundingBox());
            double density = getBlockDensity(new Vec3d(x, y, z), base.getEntityBoundingBox(), true, true);
            double densityDistance = distance = (1.0D - distance) * density;

            float damage = getDifficultyMultiplier((float) ((densityDistance * densityDistance + distance) / 2.0D * 7.0D * 12.0D + 1.0D));
            DamageSource damageSource = DamageSource.causeExplosionDamage(new Explosion(mc.world, mc.player, x, y, z, 6.0F, false, true));
            damage = CombatRules.getDamageAfterAbsorb(damage, base.getTotalArmorValue(), (float) base.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int modifierDamage = EnchantmentUtil.getEnchantmentModifierDamage(base.getArmorInventoryList(), damageSource);
            if (modifierDamage > 0)
            {
                damage = CombatRules.getDamageAfterMagicAbsorb(damage, modifierDamage);
            }

            PotionEffect resistance = base.getActivePotionEffect(MobEffects.RESISTANCE);
            if (resistance != null)
            {
                damage = damage * (25 - (resistance.getAmplifier() + 1) * 5) / 25.0F;
            }

            return Math.max(damage, 0.0F);
        }
    }

    /**
     * Creates a damage multiplier based on the worlds difficulty
     * and a given distance to a damage source.
     *
     * @param distance the distance to the damage source.
     * @return a damage multiplier.
     */
    public static float getDifficultyMultiplier(float distance)
    {
        switch (mc.world.getDifficulty())
        {
            case PEACEFUL:
                return 0.0F;
            case EASY:
                return Math.min(distance / 2.0F + 1.0F, distance);
            case HARD:
                return distance * 3.0F / 2.0F;
        }

        return distance;
    }

    /**
     * Essentially the same as {@link net.minecraft.world.World#getBlockDensity(Vec3d, AxisAlignedBB)}
     * but with an option to ignore webs and beds.
     *
     * @param vec we check the blocks along this vector.
     * @param bb the bounding box inside which we check.
     * @param ignoreWebs if you want to ignore webs.
     * @return the percentage of real blocks within the given parameters.
     */
    public static float getBlockDensity(Vec3d vec, AxisAlignedBB bb, boolean ignoreWebs, boolean ignoreBeds)
    {
        double x = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
        double y = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
        double z = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
        double xFloor = (1.0D - Math.floor(1.0D / x) * x) / 2.0D;
        double zFloor = (1.0D - Math.floor(1.0D / z) * z) / 2.0D;

        if (x >= 0.0D && y >= 0.0D && z >= 0.0D)
        {
            int air = 0;
            int traced = 0;

            for (float a = 0.0F; a <= 1.0F; a = (float) ((double) a + x))
            {
                for (float b = 0.0F; b <= 1.0F; b = (float) ((double) b + y))
                {
                    for (float c = 0.0F; c <= 1.0F; c = (float) ((double) c + z))
                    {
                        double xOff = bb.minX + (bb.maxX - bb.minX) * (double) a;
                        double yOff = bb.minY + (bb.maxY - bb.minY) * (double) b;
                        double zOff = bb.minZ + (bb.maxZ - bb.minZ) * (double) c;

                        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(xOff + xFloor, yOff, zOff + zFloor), vec);
                        if (result == null)
                        {
                            ++air;
                        }
                        else if (result.typeOfHit == RayTraceResult.Type.BLOCK)
                        {
                            Block block = mc.world.getBlockState(result.getBlockPos()).getBlock();
                            if (ignoreWebs && block instanceof BlockWeb)
                            {
                                ++air;
                            }
                            else if (ignoreBeds && block instanceof BlockBed)
                            {
                                ++air;
                            }
                        }

                        ++traced;
                    }
                }
            }

            return (float) air / (float) traced;
        }
        else
        {
            return 0.0F;
        }
    }

}
