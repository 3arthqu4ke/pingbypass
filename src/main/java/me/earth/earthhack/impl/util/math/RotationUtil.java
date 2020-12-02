package me.earth.earthhack.impl.util.math;

import me.earth.earthhack.api.util.Globals;
import me.earth.pingbypass.client.managers.RotationManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static me.earth.earthhack.impl.util.math.PositionUtil.getEyeHeight;

public class RotationUtil implements Globals
{
    public static float[] getRotations(BlockPos pos, EnumFacing facing)
    {
        AxisAlignedBB bb = mc.world.getBlockState(pos).getBoundingBox(mc.world, pos);
        double x = pos.getX() + (bb.minX + bb.maxX) / 2.0;
        double y = pos.getY() + (bb.minY + bb.maxY) / 2.0;
        double z = pos.getZ() + (bb.minZ + bb.maxZ) / 2.0;

        if (facing != null)
        {
            x += facing.getDirectionVec().getX() * ((bb.minX + bb.maxX) / 2.0);
            y += facing.getDirectionVec().getY() * ((bb.minY + bb.maxY) / 2.0);
            z += facing.getDirectionVec().getZ() * ((bb.minZ + bb.maxZ) / 2.0);
        }

        return getRotations(x, y, z);
    }

    /**
     * Convenience method calling
     * {@link RotationUtil#getRotations(double, double, double)}.
     */
    public static float[] getRotations(BlockPos pos)
    {
        return getRotations(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    /**
     * Convenience method calling
     * {@link RotationUtil#getRotations(double, double, double)}.
     */
    public static float[] getRotations(Entity entity)
    {
        return getRotations(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ);
    }

    /**
     * Convenience method calling
     * {@link RotationUtil#getRotations(double, double, double)}.
     */
    public static float[] getRotations(Vec3d vec3d)
    {
        return getRotations(vec3d.x, vec3d.y, vec3d.z);
    }

    /**
     * Returns a float array of length 2, containing
     * a yaw at index 0, and pitch at index 1 looking from
     * the player towards the given coordinates.
     *
     * @param x the x coordinate of the point you want to look at.
     * @param y the y coordinate of the point you want to look at.
     * @param z the z coordinate of the point you want to look at.
     * @return yaw and pitch in direction of the coordinate.
     */
    public static float[] getRotations(double x, double y, double z)
    {
        double xDiff = x - mc.player.posX;
        double yDiff = y - getEyeHeight();
        double zDiff = z - mc.player.posZ;
        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);

        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        float diff = yaw - mc.player.rotationYaw;

        if (diff < -180.0f || diff > 180.0f)
        {
            float round = Math.round(Math.abs(diff / 360.0f));
            diff = diff < 0.0f ? diff + 360.0f * round : diff - (360.0f * round);
        }

        return new float[] {mc.player.rotationYaw + diff, pitch};
    }
    /**
     * Transforms given yaw and pitch into a Vec3d.
     *
     * @param yaw the yaw.
     * @param pitch the pitch.
     * @return the look vector for yaw and pitch.
     */
    public static Vec3d getVec3d(float yaw, float pitch)
    {
        float vx = -MathHelper.sin(MathUtil.rad(yaw)) * MathHelper.cos(MathUtil.rad(pitch));
        float vz = MathHelper.cos(MathUtil.rad(yaw)) * MathHelper.cos(MathUtil.rad(pitch));
        float vy = -MathHelper.sin(MathUtil.rad(pitch));
        return new Vec3d(vx, vy, vz);
    }


    //We could also write our own mc.world.raytraceBlocks?
    /**
     * Convenience method, calls
     * {@link RotationUtil#isLegit(double, double, double)}.
     */
    public static boolean isLegit(Vec3d vec3d)
    {
        return isLegit(vec3d.x, vec3d.y, vec3d.z);
    }

    /**
     * Convenience method calling
     * {@link RotationUtil#isLegit(double, double, double)}.
     */
    public static boolean isLegit(Entity entity)
    {
        return isLegit(entity.posX, entity.posY + entity.getEyeHeight() / 2, entity.posZ);
    }

    /**
     * Convenience method calling
     * {@link RotationUtil#isLegit(double, double, double)}.
     */
    public static boolean isLegit(BlockPos pos)
    {
        double x = pos.getX() + 0.5;
        double y = pos.getY();// + 0.5;
        double z = pos.getZ() + 0.5;

        return isLegit(x, y, z);
    }

    /**
     * Method to check if the last reported rotation
     * of the player looks in the proximity of the given coordinates.
     * It should be noted that this is just a simple approximation.
     *
     * @param x x-coordinate of the point.
     * @param y y-coordinate of the point.
     * @param z z-coordinate of the point.
     * @return <tt>true</tt> if the player is looking at the block.
     */
    public static boolean isLegit(double x, double y, double z)
    {
        return isLegit(x, y, z, RotationManager.getInstance().getYaw(), RotationManager.getInstance().getPitch());
    }

    /**
     * Method to check if the given yaw and pitch
     * result in a legit rotation to the block.
     *
     * @param x x-coordinate of the point.
     * @param y y-coordinate of the point.
     * @param z z-coordinate of the point.
     * @param yaw the yaw.
     * @param pitch the pitch.
     * @return <tt>true</tt> if yaw and pitch look at the block.
     */
    //TODO: raytrace would be way better here.
    public static boolean isLegit(double x, double y, double z, float yaw, float pitch)
    {
        Vec3d vec3d = MathUtil.fromTo(PositionUtil.getEyePos(), x, y, z);
        double dist = vec3d.length();
        if (dist < 0.5)
        {
            return true;
        }

        return MathUtil.angle(vec3d, getVec3d(yaw, pitch)) * dist < 35;
    }
}
