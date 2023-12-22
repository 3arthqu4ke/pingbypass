package me.earth.pingbypass.commons.mixins.network;

import me.earth.pingbypass.commons.ducks.network.IConnectionProtocol;
import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ConnectionProtocol.class)
public abstract class MixinConnectionProtocol implements IConnectionProtocol {
    @Final
    @Shadow
    private Map<PacketFlow, IPacketSet> flows;

    @Override
    public int pingbypass_getId(PacketFlow packetFlow, Class<? extends Packet<?>> packet) {
        return flows.get(packetFlow).invokeGetId(packet);
    }

    @Inject(method = "getProtocolForPacket", at = @At("HEAD"), cancellable = true)
    private static void getProtocolForPacketHook(Packet<?> packet, CallbackInfoReturnable<ConnectionProtocol> cir) {
        if (packet instanceof CustomPacket customPacket) {
            cir.setReturnValue(customPacket.getProtocol());
        }
    }

    @Inject(method = "getPacketId", at = @At("HEAD"), cancellable = true)
    public void getPacketIdHook(PacketFlow flow, Packet<?> packet, CallbackInfoReturnable<@Nullable Integer> cir) {
        if (packet instanceof CustomPacket customPacket) {
            cir.setReturnValue(customPacket.getId());
        }
    }

}
