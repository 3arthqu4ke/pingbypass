package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.earthhack.impl.util.math.PositionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDestroyEntities;

public class DestroyEntitiesListener extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketDestroyEntities>>
{
    protected DestroyEntitiesListener(AutoCrystal module)
    {
        super(module, PacketEvent.Receive.class, SPacketDestroyEntities.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketDestroyEntities> event)
    {
        mc.addScheduledTask(() ->
        {
            if (mc.world != null)
            {
                SPacketDestroyEntities packet = event.getPacket();
                for (int id : packet.getEntityIDs())
                {
                    Entity entity = mc.world.getEntityByID(id);
                    if (entity instanceof EntityEnderCrystal)
                    {
                        module.positions.remove(PositionUtil.getPosition(entity));
                    }
                }
            }
        });
    }

}
