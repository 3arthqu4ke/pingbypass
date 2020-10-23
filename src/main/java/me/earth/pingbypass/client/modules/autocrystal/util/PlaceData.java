package me.earth.pingbypass.client.modules.autocrystal.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class PlaceData
{
    private final List<Entity> entities;
    private float damage;
    private float selfDamage = -2;
    private EntityPlayer target;
    private BlockPos pos;


    public PlaceData(EntityPlayer target, List<Entity> crystals)
    {
        this.target = target;
        this.entities = crystals;
    }

    public EntityPlayer getTarget()
    {
        return target;
    }

    public void setTarget(EntityPlayer target)
    {
        this.target = target;
    }

    public float getDamage()
    {
        return damage;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public void setPos(BlockPos pos)
    {
        this.pos = pos;
    }

    public float getSelfDamage()
    {
        return selfDamage;
    }

    public void setSelfDamage(float selfDamage)
    {
        this.selfDamage = selfDamage;
    }

    public List<Entity> getEntities()
    {
        return entities;
    }

}
