package me.earth.pingbypass.api.mixins.network.s2c;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundExplodePacket.class)
public interface IClientBoundExplodePacket {
    @Mutable
    @Accessor("knockbackX")
    void setKnockbackX(float x);

    @Mutable
    @Accessor("knockbackY")
    void setKnockbackY(float y);

    @Mutable
    @Accessor("knockbackZ")
    void setKnockbackZ(float z);

}
