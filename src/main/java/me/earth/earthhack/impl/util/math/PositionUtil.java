package me.earth.earthhack.impl.util.math;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

public class PositionUtil implements Globals
{
    public static BlockPos getPosition()
    {
        return getPosition(mc.player);
    }

    public static BlockPos getPosition(Entity entity)
    {
        return new BlockPos(entity.posX, entity.posY, entity.posZ);
    }

    public static Vec3d getEyePos()
    {
        return getEyePos(mc.player);
    }

    public static Vec3d getEyePos(Entity entity)
    {
        return new Vec3d(entity.posX, getEyeHeight(entity), entity.posZ);
    }

    public static double getEyeHeight()
    {
        return getEyeHeight(mc.player);
    }

    public static double getEyeHeight(Entity entity)
    {
        return entity.posY + entity.getEyeHeight();
    }

    public static Set<BlockPos> getBlockedPositions(Entity entity)
    {
        Set<BlockPos> positions = new HashSet<>();
        AxisAlignedBB bb = entity.getEntityBoundingBox();

        positions.add(new BlockPos(bb.maxX, bb.minY, bb.maxZ));
        positions.add(new BlockPos(bb.minX, bb.minY, bb.minZ));
        positions.add(new BlockPos(bb.maxX, bb.minY, bb.minZ));
        positions.add(new BlockPos(bb.minX, bb.minY, bb.maxZ));

        return positions;
    }

}
