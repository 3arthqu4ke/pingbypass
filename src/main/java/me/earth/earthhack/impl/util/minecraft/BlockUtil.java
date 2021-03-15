package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.math.PositionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.EnumFacing.HORIZONTALS;

public class BlockUtil implements Globals
{
    public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreCrystals, boolean noBoost2, List<Entity> entities)
    {
        if (mc.world.getBlockState(pos).getBlock() != Blocks.OBSIDIAN && mc.world.getBlockState(pos).getBlock() != Blocks.BEDROCK)
        {
            return false;
        }

        BlockPos boost  = pos.up();

        if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || !checkEntityList(boost, ignoreCrystals, entities))
        {
            return false;
        }

        if (!noBoost2)
        {
            BlockPos boost2 = boost.up();

            if (mc.world.getBlockState(boost2).getBlock() != Blocks.AIR)
            {
                return false;
            }

            return checkEntityList(boost2, ignoreCrystals, entities);
        }

        return true;
    }

    /**
     * Returns <tt>true</tt> if a position exists
     * such that you can place a bed whose headpiece is
     * on the given position.
     *
     * @param pos the given position.
     * @param newerVer taking 1.13+ mechanic into account.
     * @return <tt>true</tt> if bed can be placed.
     */
    public static boolean canPlaceBed(BlockPos pos, boolean newerVer)
    {
        if (!bedBlockCheck(pos, newerVer))
        {
            return false;
        }

        for (EnumFacing facing : HORIZONTALS)
        {
            BlockPos horizontal = pos.offset(facing);
            if (bedBlockCheck(horizontal, newerVer) && getFacing(horizontal) != null)
            {
                return true;
            }
        }

        return false;
    }

    public static List<BlockPos> getSphere(float radius)
    {
        return getSphere(PositionUtil.getPosition(), radius);
    }

    public static List<BlockPos> getSphere(BlockPos pos, float radius)
    {
        List<BlockPos> sphere = new ArrayList<>();

        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();

        for (int x = posX - (int) radius; x <= posX + radius; x++)
        {
            for (int z = posZ - (int) radius; z <= posZ + radius; z++)
            {
                for (int y = posY - (int) radius; y < posY + radius; y++)
                {
                    double dist = (posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y);
                    if (dist < radius * radius)
                    {
                        BlockPos position = new BlockPos(x, y, z);
                        sphere.add(position);
                    }
                }
            }
        }

        return sphere;
    }

    public static boolean checkEntityList(BlockPos pos, boolean ignoreCrystals, List<Entity> entities)
    {
        if (entities == null)
        {
            return checkEntities(pos, ignoreCrystals);
        }

        for (Entity entity : entities)
        {
            if (entity != null && !(EntityUtil.isDead(entity) || ignoreCrystals && entity instanceof EntityEnderCrystal))
            {
                if (entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos)))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean isAir(BlockPos pos)
    {
        return mc.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    public static boolean checkEntities(BlockPos pos, boolean ignoreCrystals)
    {
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)))
        {
            if (EntityUtil.isDead(entity) || ignoreCrystals && entity instanceof EntityEnderCrystal)
            {
                continue;
            }

            return false;
        }

        return true;
    }

    public static EnumFacing getFacing(BlockPos pos)
    {
        for (EnumFacing facing : EnumFacing.values())
        {
            if (!mc.world.getBlockState(pos.offset(facing)).getMaterial().isReplaceable())
            {
                return facing;
            }
        }

        return null;
    }

    private static boolean bedBlockCheck(BlockPos pos, boolean newerVer)
    {
        return mc.world.getBlockState(pos).getMaterial().isReplaceable() && (newerVer || !mc.world.getBlockState(pos.down()).getMaterial().isReplaceable());
    }

    public static void placeBlockMainHand(BlockPos pos) {
        placeBlock(EnumHand.MAIN_HAND, pos);
    }

    /**
     * Convenience Function.
     * Places blocks using mainhand.
     *
     * @param hand, mainhand used to place blocks.
     * @param pos, the position where the mainhand will be used.
     */

    public static void placeBlock(EnumHand hand, BlockPos pos) {
        Vec3d eyesPos = new Vec3d(mc.player.posX,
                mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ);

        for(EnumFacing side: EnumFacing.values()) {
            BlockPos blockToPlace = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            /**
             * Check if we can place the block on the position.
             */
            if(!mc.world.getBlockState(blockToPlace).getBlock().canCollideCheck(mc.world.getBlockState(blockToPlace), false))
                continue;

            Vec3d hitVec = new Vec3d(blockToPlace).add(0.5, 0.5, 0.5)
                    .add(new Vec3d(side2.getDirectionVec()).scale(0.5));
            /**
             * Check if the place / hit Vector is within 4.25 blocks.
             * If yes, then it continue the function.
             */
            if(eyesPos.squareDistanceTo(hitVec) > 18.0625)
                continue;

            /**
             * Do the magic, and place the block.
             */

            double diffX = hitVec.x - eyesPos.x;
            double diffY = hitVec.y - eyesPos.y;
            double diffZ = hitVec.z - eyesPos.z;

            double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

            float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
            float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

            float[] rotations = {
                    mc.player.rotationYaw
                            + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
                    mc.player.rotationPitch + MathHelper
                            .wrapDegrees(pitch - mc.player.rotationPitch)};

            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0],
                    rotations[1], mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.playerController.processRightClickBlock(mc.player,
                    mc.world, blockToPlace, side2, hitVec, hand);
            mc.player.swingArm(hand);
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
    }
}
