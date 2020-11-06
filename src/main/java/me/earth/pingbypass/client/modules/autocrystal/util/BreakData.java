package me.earth.pingbypass.client.modules.autocrystal.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class BreakData
{
    private float damage;
    private Entity crystal;
    private int count;
    private Entity fallBack;
    private float fallBackDamage = 1000.0f;
    private EntityPlayer target;

    public int getCount()
    {
        return count;
    }

    public void increment()
    {
        count++;
    }

    public float getDamage()
    {
        return damage;
    }

    public Entity getCrystal()
    {
        return crystal;
    }

    public Entity getFallBack()
    {
        return fallBack;
    }

    public void setFallBack(Entity fallBack, float damage)
    {
        if (this.fallBackDamage > damage)
        {
            this.fallBackDamage = damage;
            this.fallBack = fallBack;
        }
    }

    public float getFallBackDamage()
    {
        return this.fallBackDamage;
    }

    public void setCrystal(Entity crystal)
    {
        this.crystal = crystal;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    public EntityPlayer getTarget()
    {
        return target;
    }

    public void setTarget(EntityPlayer target)
    {
        this.target = target;
    }

}
