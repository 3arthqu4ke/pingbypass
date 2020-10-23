package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.math.MathUtil;
import me.earth.pingbypass.client.managers.FriendManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityUtil implements Globals
{
    /**
     * Checks if {@link Entity#isDead} is true
     * for the given entity, or if the entity
     * extends EntityLiving base, its health is
     * lower than 0;
     *
     * @param entity the given entity.
     * @return <tt>true</tt> if the entity is dead.
     */
    public static boolean isDead(Entity entity)
    {
        return entity.isDead
                || entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() <= 0.0f;
    }

    /**
     * Returns the full health (health + absorption amount)
     * for the give entity.
     *
     * @param base the player.
     * @return health + absorption amount.
     */
    public static float getHealth(EntityLivingBase base)
    {
        return base.getHealth() + base.getAbsorptionAmount();
    }

    /**
     * Convenience method, calls {@link EntityUtil#getClosestEnemy(Vec3d, List)}
     * for the players positionVector and mc.world.playerEntities.
     */
    public static EntityPlayer getClosestEnemy()
    {
        return getClosestEnemy(mc.world.playerEntities);
    }

    /**
     * Convenience method, calls {@link EntityUtil#getClosestEnemy(Vec3d, List)}
     * for the players positionVector and the given list.
     */
    public static EntityPlayer getClosestEnemy(List<EntityPlayer> list)
    {
        return getClosestEnemy(mc.player.getPositionVector(), list);
    }

    /**
     * Convenience method, calls {@link EntityUtil#getClosestEnemy(double, double, double, List)}
     * for the coordinates of the position and the given list.
     */
    public static EntityPlayer getClosestEnemy(BlockPos pos, List<EntityPlayer> list)
    {
        return getClosestEnemy(pos.getX(), pos.getY(), pos.getZ(), list);
    }

    /**
     * Convenience method, calls {@link EntityUtil#getClosestEnemy(double, double, double, List)}
     * for the coordinates of the vec3d and the given list.
     */
    public static EntityPlayer getClosestEnemy(Vec3d vec3d, List<EntityPlayer> list)
    {
        return getClosestEnemy(vec3d.x, vec3d.y, vec3d.z, list);
    }

    /**
     * Returns the closest not friended EntityPlayer to the given
     * coords from the given list.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @param list the list in which we look for the entity.
     * @return the closest player from the list.
     */
    public static EntityPlayer getClosestEnemy(double x, double y, double z, List<EntityPlayer> list)
    {
        EntityPlayer closest = null;
        double distance = 10000.0f;

        for (EntityPlayer player : list)
        {
            if (player != null)
            {
                double dist = player.getDistance(x, y, z);
                if (!EntityUtil.isDead(player) && dist < distance && !FriendManager.getInstance().isFriend(player))
                {
                    distance = dist;
                    closest = player;
                }
            }
        }

        return closest;
    }

    /**
     * Returns true if the give player is != null,
     * not died, not friended and within the given range.
     *
     * @param player the player to check.
     * @param range the range he should be in.
     * @return <tt>true</tt> if the player fulfills the conditions.
     */
    public static boolean isValid(EntityPlayer player, double range)
    {
        return player != null && !EntityUtil.isDead(player) && mc.player.getDistanceSq(player) <= MathUtil.square(range) && !FriendManager.getInstance().isFriend(player);
    }

}
