package me.earth.pingbypass.api.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface PBPacket extends Packet<PacketListener> {
    ResourceLocation getId();

    void writePacket(FriendlyByteBuf buf);

    @Override
    default void handle(PacketListener handler) {
        // handling happens through the ProtocolManager
    }

    record Payload(PBPacket packet) implements CustomPacketPayload {
        @Override
        public void write(FriendlyByteBuf buffer) {
            packet.writePacket(buffer);
        }

        @Override
        public @NotNull ResourceLocation id() {
            return packet.getId();
        }
    }

}
