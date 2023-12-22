package me.earth.pingbypass.commons.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

@FunctionalInterface
public interface PacketFactory<T extends PacketListener> {
    Packet<T> create(FriendlyByteBuf buf);

}
