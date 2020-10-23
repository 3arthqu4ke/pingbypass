package me.earth.pingbypass.client.modules.autocrystal.util;

import net.minecraft.entity.Entity;

public class BreakData
{
    private float damage;
    private Entity crystal;
    private int count;
    private Entity fallBack;
    private float fallBackDamage = 1000.0f;

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

}
