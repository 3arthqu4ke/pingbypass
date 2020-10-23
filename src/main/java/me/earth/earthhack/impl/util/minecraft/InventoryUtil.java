package me.earth.earthhack.impl.util.minecraft;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.mixin.ducks.IPlayerControllerMP;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil implements Globals
{
    public static void switchTo(int slot)
    {
        if (mc.player.inventory.currentItem != slot && slot > -1 && slot < 9)
        {
            mc.player.inventory.currentItem = slot;
            syncItem();
        }
    }

    @SuppressWarnings("Duplicates")
    public static int findHotbarBlock(Block block)
    {
        if (ItemUtil.areSame(mc.player.getHeldItemOffhand(), block))
        {
            return -2;
        }

        int result = -1;
        for (int i = 9; i > 0; i--)
        {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (ItemUtil.areSame(stack, block))
            {
                result = i;
                if (mc.player.inventory.currentItem == i)
                {
                    break;
                }
            }
        }

        return result;
    }

    @SuppressWarnings("Duplicates")
    public static int findHotbarItem(Item item)
    {
        if (ItemUtil.areSame(mc.player.getHeldItemOffhand(), item))
        {
            return -2;
        }

        int result = -1;
        for (int i = 9; i > 0; i--)
        {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (ItemUtil.areSame(stack, item))
            {
                result = i;
                if (mc.player.inventory.currentItem == i)
                {
                    break;
                }
            }
        }

        return result;
    }

    public static int findItem(Item item, boolean xCarry)
    {
        if (mc.player.inventory.getItemStack().getItem() == item)
        {
            return -2;
        }

        for (int i = 9; i < 45; i++)
        {
            ItemStack stack = mc.player.inventoryContainer.getInventory().get(i);
            if (stack.getItem() == item)
            {
                return i;
            }
        }

        if (xCarry)
        {
            for (int i = 1; i < 5; i++)
            {
                ItemStack stack = mc.player.inventoryContainer.getInventory().get(i);
                if (stack.getItem() == item)
                {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int getCount(Item item)
    {
        int result = 0;
        for (int i = 0; i < 46; i++)
        {
            ItemStack stack = mc.player.inventoryContainer.getInventory().get(i);
            if (stack.getItem() == item)
            {
                result += stack.getCount();
            }
        }

        if (mc.player.inventory.getItemStack().getItem() == item)
        {
            result += mc.player.inventory.getItemStack().getCount();
        }

        return result;
    }

    public static boolean isHoldingServer(Item item)
    {
        ItemStack offHand  = mc.player.getHeldItemOffhand();
        if (ItemUtil.areSame(offHand, item))
        {
            return true;
        }

        ItemStack mainHand = mc.player.getHeldItemMainhand();
        if (ItemUtil.areSame(mainHand, item))
        {
            int current = mc.player.inventory.currentItem;
            int server  = getServerItem();
            return server == current;
        }

        return false;
    }

    public static boolean isHolding(Item item)
    {
        return isHolding(mc.player, item);
    }

    public static boolean isHolding(Block block)
    {
        return isHolding(mc.player, block);
    }

    public static boolean isHolding(EntityLivingBase entity, Item item)
    {
        ItemStack mainHand = entity.getHeldItemMainhand();
        ItemStack offHand  = entity.getHeldItemOffhand();

        return ItemUtil.areSame(mainHand, item) || ItemUtil.areSame(offHand, item);
    }

    public static boolean isHolding(EntityLivingBase entity, Block block)
    {
        ItemStack mainHand = entity.getHeldItemMainhand();
        ItemStack offHand  = entity.getHeldItemOffhand();

        return ItemUtil.areSame(mainHand, block) || ItemUtil.areSame(offHand, block);
    }

    public static int getServerItem()
    {
        return ((IPlayerControllerMP) mc.playerController).getItem();
    }

    public static void syncItem()
    {
        ((IPlayerControllerMP) mc.playerController).syncItem();
    }

}
