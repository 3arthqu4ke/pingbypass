package me.earth.pingbypass.api.protocol.event;

import lombok.Data;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Fired when a {@link ServerboundCustomPayloadPacket} or a {@link ClientboundCustomPayloadPacket} is being constructed.
 * Call {@link #setPayload(CustomPacketPayload)} to intercept the creation and create a custom {@link CustomPacketPayload}.
 */
@Data
public class CustomPayloadInitEvent {
    private final Class<? extends Packet<?>> type;
    private final ResourceLocation location;
    private final FriendlyByteBuf buf;
    private @Nullable CustomPacketPayload payload;

}
