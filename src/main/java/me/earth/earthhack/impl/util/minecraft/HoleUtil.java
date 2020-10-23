package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.HashSet;
import java.util.Set;

import static me.earth.earthhack.impl.util.minecraft.BlockUtil.isAir;
import static net.minecraft.util.EnumFacing.HORIZONTALS;

public class HoleUtil implements Globals
{
    /** Offsets for a 2x2 hole. */
    private static final Vec3i[] OFFSETS_2x2 = new Vec3i[]
        {
            new Vec3i(0, 0, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(1, 0, 1)
        };

    /**
     * Returns a boolean array of length 2,
     * where index 0 represents if the given
     * position is a hole and index 1 represents
     * if the given hole is safe (full bedrock) or
     * not.
     *
     * @param pos the given position.
     * @return a boolean array.
     */
    public static boolean[] isHole(BlockPos pos)
    {
        boolean[] result = new boolean[]{false, true};
        if (!isAir(pos) || !isAir(pos.up()) || !isAir(pos.up(2)))
        {
            return result;
        }

        for (EnumFacing facing : EnumFacing.values())
        {
            if (facing != EnumFacing.UP)
            {
                BlockPos offset = pos.offset(facing);
                if (mc.world.getBlockState(offset).getBlock() != Blocks.BEDROCK)
                {
                    if (mc.world.getBlockState(offset).getMaterial().isReplaceable())
                    {
                        return result;
                    }

                    result[1] = false;
                }
            }
        }

        result[0] = true;
        return result;
    }

    /**
     * Returns <tt>true</tt> if the given position
     * is a long (2x1) hole.
     *
     * @param pos the given position.
     * @return <tt>true</tt> if a 2x1 hole.
     */
    public static boolean is2x1(BlockPos pos)
    {
        if (!isAir(pos) || !isAir(pos.up()) || !isAir(pos.up(2)) || isAir(pos.down()))
        {
            return false;
        }

        int airBlocks = 0;
        for (EnumFacing facing : HORIZONTALS)
        {
            BlockPos offset = pos.offset(facing);
            if (isAir(offset) && isAir(offset.up()))
            {
                if (!isAir(offset.down()))
                {
                    for (EnumFacing offsetFacing: HORIZONTALS)
                    {
                        if (offsetFacing != facing.getOpposite())
                        {
                            if (isAir(offset.offset(offsetFacing)))
                            {
                                return false;
                            }
                        }
                    }
                }
                else
                {
                    return false;
                }

                airBlocks++;
            }

            if (airBlocks > 1)
            {
                return false;
            }
        }

        return airBlocks == 1;
    }

    /**
     * Returns <tt>true</tt> if the given position
     * is part of a 2x2 hole, more accurately if the
     * given position is the bottom left part of the
     * hole. The other 3 positions have the
     * offsets (0,0,1), (1,0,0) and (1,0,1).
     *
     * @param pos the given position.
     * @return <tt>true</tt> if a 2x2 hole.
     */
    public static boolean is2x2(BlockPos pos)
    {
        Set<BlockPos> positions = new HashSet<>();
        for (Vec3i vec : OFFSETS_2x2)
        {
            positions.add(pos.add(vec));
        }

        boolean airBlock = false;
        for (BlockPos holePos : positions)
        {
            if (isAir(holePos) && isAir(holePos.up()) && !isAir(holePos.down()))
            {
                if (isAir(holePos.up(2)))
                {
                    airBlock = true;
                }

                for (EnumFacing facing : HORIZONTALS)
                {
                    BlockPos offset = holePos.offset(facing);
                    if (!positions.contains(offset))
                    {
                        if (isAir(offset))
                        {
                            return false;
                        }
                    }
                }
            }
            else
            {
                return false;
            }
        }

        return airBlock;
    }

}
