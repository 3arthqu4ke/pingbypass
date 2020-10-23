package me.earth.earthhack.impl.util.thread;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;

/**
 * The part of minecraft's EnchantmentHelper needed
 * to calculate Explosion Damage for AutoCrystal etc..
 * But implemented in a way that allows you to access
 * it from multiple threads at the same time. Still not
 * safe regarding the list of item stacks.
 */
public class EnchantmentUtil
{
    public static int getEnchantmentModifierDamage(Iterable<ItemStack> stacks, DamageSource source)
    {
        int modifier = 0;
        for (ItemStack stack : stacks)
        {
            if (!stack.isEmpty())
            {
                NBTTagList nbttaglist = stack.getEnchantmentTagList();
                for (int i = 0; i < nbttaglist.tagCount(); ++i)
                {
                    int j = nbttaglist.getCompoundTagAt(i).getShort("id");
                    int k = nbttaglist.getCompoundTagAt(i).getShort("lvl");
                    Enchantment enchantment = Enchantment.getEnchantmentByID(j);

                    if (enchantment != null)
                    {
                        modifier += enchantment.calcModifierDamage(k, source);
                    }
                }
            }
        }

        return modifier;
    }

}
