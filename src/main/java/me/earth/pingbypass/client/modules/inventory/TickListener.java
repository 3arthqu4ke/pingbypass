package me.earth.pingbypass.client.modules.inventory;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.TickEvent;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.pingbypass.PingBypass;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;

//TODO: make all Listeners package private
public class TickListener extends ModuleListener<InventoryModule, TickEvent>
{
    private final StopWatch timer = new StopWatch();

    protected TickListener(InventoryModule module)
    {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event)
    {
        if (mc.player != null && timer.passed(module.delay.getValue() * 1000))
        {
            ItemStack offhand = mc.player.getHeldItemOffhand().copy();
            PingBypass.server.sendToClient(new SPacketSetSlot(-2, 40, offhand));

            ItemStack cursor = mc.player.inventory.getItemStack().copy();
            PingBypass.server.sendToClient(new SPacketSetSlot(-1, -1, cursor));

            for (int i = 36; i < 40; i++)
            {
                ItemStack stack = mc.player.inventory.getStackInSlot(i).copy();
                PingBypass.server.sendToClient(new SPacketSetSlot(-2, i, stack));
            }

            timer.reset();
        }
    }

}

