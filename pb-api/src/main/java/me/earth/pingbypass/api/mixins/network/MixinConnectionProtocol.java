package me.earth.pingbypass.api.mixins.network;

import me.earth.pingbypass.api.ducks.network.IConnectionProtocol;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

}
