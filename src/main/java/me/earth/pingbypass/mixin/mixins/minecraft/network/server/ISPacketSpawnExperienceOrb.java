package me.earth.pingbypass.mixin.mixins.minecraft.network.server;

import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SPacketSpawnExperienceOrb.class)
public interface ISPacketSpawnExperienceOrb
{
    @Accessor(value = "entityID")
    void setEntityId(int entityId);

    @Accessor(value = "posX")
    void setX(double x);

    @Accessor(value = "posY")
    void setY(double y);

    @Accessor(value = "posZ")
    void setZ(double z);

    @Accessor(value = "xpValue")
    void setExp(int expValue);

}
