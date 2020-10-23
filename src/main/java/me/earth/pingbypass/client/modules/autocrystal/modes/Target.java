package me.earth.pingbypass.client.modules.autocrystal.modes;

import me.earth.earthhack.impl.util.minecraft.EntityUtil;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public enum Target
{
    Closest
    {
        @Override
        public EntityPlayer getTarget(List<EntityPlayer> players)
        {
            return EntityUtil.getClosestEnemy(players);
        }
    },
    Damage
    {
        @Override
        public EntityPlayer getTarget(List<EntityPlayer> players)
        {
            return null;
        }
    };

    public abstract EntityPlayer getTarget(List<EntityPlayer> players);
}
