package me.earth.pingbypass.commons.protocol;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;

public interface PbPacket<T extends PacketListener> {
    ResourceLocation RESOURCE = new ResourceLocation("ping", "bypass");

    void writeInnerBuffer(FriendlyByteBuf buf);

    void process(T listener);

}
