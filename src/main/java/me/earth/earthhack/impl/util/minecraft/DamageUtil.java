package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.thread.EnchantmentUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
     * to the targeted EntityLivingBase. Note that beds(0.5, 0.5, 0.5) explode
     * with a different offset than crystals(0.5, 1, 0.5) at their headpiece.
     * Beds also don't create the same explosion size, but we ignore that for
     * the sake of minDamage settings being the same for all calculations.
     * (FeetPlace does same damage anyways).
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

                        RayTraceResult result = rayTraceBlocks(new Vec3d(xOff + xFloor, yOff, zOff + zFloor), vec, false, false, false, ignoreWebs, ignoreBeds);
                        if (result == null)
                        {
                            air++;
                        }

                        traced++;
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

    /**
     * Essentially the same as {@link net.minecraft.world.World#rayTraceBlocks(Vec3d, Vec3d, boolean, boolean, boolean)} (Vec3d, AxisAlignedBB)}
     * but with an option to ignore webs and beds.
     *
     * @param vec31 same as the original param.
     * @param vec32 same as the original param.
     * @param stopOnLiquid same as the original param.
     * @param ignoreNoBox same as the original param.
     * @param returnLastUncollidableBlock same as the original param.
     * @param ignoreWebs handles webs like air.
     * @param ignoreBeds handles beds like air.
     * @return {@link net.minecraft.world.World#rayTraceBlocks(Vec3d, Vec3d, boolean, boolean, boolean)} (Vec3d, AxisAlignedBB)} for the given params.
     */
    public static RayTraceResult rayTraceBlocks(Vec3d vec31,
                                                Vec3d vec32,
                                                boolean stopOnLiquid,
                                                boolean ignoreNoBox,
                                                boolean returnLastUncollidableBlock,
                                                boolean ignoreWebs,
                                                boolean ignoreBeds)
    {
        if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z))
        {
            if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z))
            {
                int x1 = MathHelper.floor(vec31.x);
                int y1 = MathHelper.floor(vec31.y);
                int z1 = MathHelper.floor(vec31.z);
                int x2 = MathHelper.floor(vec32.x);
                int y2 = MathHelper.floor(vec32.y);
                int z2 = MathHelper.floor(vec32.z);

                BlockPos pos = new BlockPos(x1, y1, z1);
                IBlockState state = mc.world.getBlockState(pos);
                Block block = state.getBlock();

                if ((!ignoreNoBox || state.getCollisionBoundingBox(mc.world, pos) != Block.NULL_AABB)
                        && (block.canCollideCheck(state, stopOnLiquid)
                        && !(ignoreBeds && block instanceof BlockBed || ignoreWebs && block instanceof BlockWeb)))
                {
                    RayTraceResult raytraceresult = state.collisionRayTrace(mc.world, pos, vec31, vec32);
                    //noinspection ConstantConditions
                    if (raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z))
                    {
                        return null;
                    }

                    if (x1 == x2 && y1 == y2 && z1 == z2)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (x2 > x1)
                    {
                        d0 = (double)x1 + 1.0D;
                    }
                    else if (x2 < x1)
                    {
                        d0 = (double)x1 + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (y2 > y1)
                    {
                        d1 = (double)y1 + 1.0D;
                    }
                    else if (y2 < y1)
                    {
                        d1 = (double)y1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (z2 > z1)
                    {
                        d2 = (double)z1 + 1.0D;
                    }
                    else if (z2 < z1)
                    {
                        d2 = (double)z1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.x - vec31.x;
                    double d7 = vec32.y - vec31.y;
                    double d8 = vec32.z - vec31.z;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.x) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.y) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.z) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = x2 > x1 ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = y2 > y1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
                    }
                    else
                    {
                        enumfacing = z2 > z1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
                    }

                    x1 = MathHelper.floor(vec31.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    y1 = MathHelper.floor(vec31.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    z1 = MathHelper.floor(vec31.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    pos = new BlockPos(x1, y1, z1);
                    IBlockState state1 = mc.world.getBlockState(pos);
                    Block block1 = state1.getBlock();

                    if (!ignoreNoBox || state1.getMaterial() == Material.PORTAL || state1.getCollisionBoundingBox(mc.world, pos) != Block.NULL_AABB)
                    {
                        if (block1.canCollideCheck(state1, stopOnLiquid) && !(ignoreBeds && block1 instanceof BlockBed || ignoreWebs && block1 instanceof BlockWeb))
                        {
                            RayTraceResult raytraceresult1 = state1.collisionRayTrace(mc.world, pos, vec31, vec32);
                            //noinspection ConstantConditions
                            if (raytraceresult1 != null)
                            {
                                return raytraceresult1;
                            }
                        }
                        else
                        {
                            raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, pos);
                        }
                    }
                }

                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

}
