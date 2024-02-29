package me.earth.pingbypass.api.mixins.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = "net.minecraft.network.ConnectionProtocol$PacketSet")
public interface IPacketSet {
    @Invoker("getId")
    int invokeGetId(Class<?> class_);

}
