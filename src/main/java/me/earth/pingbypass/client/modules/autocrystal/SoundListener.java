package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.PacketEvent;
import me.earth.earthhack.impl.util.math.PositionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

public class SoundListener extends ModuleListener<AutoCrystal, PacketEvent.Receive<SPacketSoundEffect>>
{
    protected SoundListener(AutoCrystal module)
    {
        super(module, PacketEvent.Receive.class, SPacketSoundEffect.class);
    }

    @Override
    public void invoke(PacketEvent.Receive<SPacketSoundEffect> event)
    {
        SPacketSoundEffect packet = event.getPacket();
        if (packet.getCategory() == SoundCategory.BLOCKS
                && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
        {
            BlockPos pos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
            /*if (module.positions.remove(pos))
            {
                module.confirmed = true;
            }*/

            if (module.soundR.getValue())
            {
                killEntities(pos);
            }
        }
    }

    private void killEntities(BlockPos pos)
    {
        mc.addScheduledTask(() ->
        {
            for (Entity entity : mc.world.loadedEntityList)
            {
                if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(pos) <= 36)
                {
                    entity.setDead();
                }
            }
        });
    }

}
