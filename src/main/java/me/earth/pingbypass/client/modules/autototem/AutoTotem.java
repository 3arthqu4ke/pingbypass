package me.earth.pingbypass.client.modules.autototem;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.util.minecraft.InventoryUtil;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.managers.safety.SafetyManager;
import me.earth.pingbypass.util.wrappers.SPacketWrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.server.SPacketSetSlot;

/**
 * A soft Autototem.
 */
public class AutoTotem extends Module
{
    private static final AutoTotem INSTANCE = new AutoTotem();

    final Setting<Float> health   = register(new NumberSetting<>("Health", 14.5f, 0.0f, 36.0f));
    final Setting<Float> sHealth  = register(new NumberSetting<>("SafeHealth", 3.0f, 0.0f, 36.0f));
    final Setting<Boolean> xCarry = register(new BooleanSetting("XCarry", false));

    private AutoTotem()
    {
        super("S-AutoTotem", Category.Client);
        this.listeners.add(new GameLoopListener(this));
        this.listeners.add(new ClickWindowListener(this));
    }

    public static AutoTotem getInstance()
    {
        return INSTANCE;
    }

    protected void doAutoTotem()
    {
        if (!InventoryUtil.isHolding(Items.TOTEM_OF_UNDYING))
        {
            int slot = InventoryUtil.findItem(Items.TOTEM_OF_UNDYING, xCarry.getValue());
            if (slot != -1)
            {
                if (slot != -2)
                {
                    windowClickServer(0, slot, 0, ClickType.PICKUP, mc.player);
                }

                windowClickServer(0, 45, 0, ClickType.PICKUP, mc.player);
            }
        }
    }

    @SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
    private ItemStack windowClickServer(int windowId, int slotId, int mouseButton, ClickType type, EntityPlayer player)
    {
        short transactionID = player.openContainer.getNextTransactionID(player.inventory);
        ItemStack before    = player.inventory.getItemStack().copy();
        ItemStack itemstack = player.openContainer.slotClick(slotId, mouseButton, type, player).copy();

        //TODO: make this like not horrible
        SPacketSetSlot setSlot  = new SPacketSetSlot(-128, slotId, before); // SPacketSetSlot can't set the XCarry so I'll just make a custom packet.
        SPacketSetSlot setMouse = new SPacketSetSlot(transactionID, -1337, itemstack); // update transaction id and set mouse slot.
        PingBypass.server.sendToClient(setSlot);
        PingBypass.server.sendToClient(setMouse);

        CPacketClickWindow packet = new CPacketClickWindow(windowId, slotId, mouseButton, type, itemstack, transactionID);
        mc.player.connection.sendPacket(packet);

        return itemstack;
    }

    protected boolean isDangerous(float healthIn)
    {
        return (SafetyManager.getInstance().isSafe()
                && healthIn <= sHealth.getValue())
                || healthIn <= health.getValue();
    }

    protected boolean badScreen()
    {
        return (mc.currentScreen instanceof GuiContainer && !(mc.currentScreen instanceof GuiInventory));
    }

}
