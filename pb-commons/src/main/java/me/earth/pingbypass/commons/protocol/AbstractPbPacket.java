package me.earth.pingbypass.commons.protocol;

import me.earth.pingbypass.commons.util.packet.CustomPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPbPacket<T extends PacketListener>
        extends ClientboundCustomPayloadPacket
        implements CustomPacket, PbPacket<T> {
    private final PacketType type;
    private final int id;

    public AbstractPbPacket(ResourceLocation arg, FriendlyByteBuf arg2, PacketType type, int id) {
        super(arg, arg2);
        this.type = type;
        this.id = id;
    }

    public AbstractPbPacket(FriendlyByteBuf arg, PacketType type, int id) {
        super(arg);
        this.type = type;
        this.id = id;
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        synchronized (this.getData()) {
            this.getData().writeVarInt(id);
            this.writeInnerBuffer(this.getData());
            super.write(buf);
        }
    }

    @Override
    public Integer getId() {
        return type.getPacketId();
    }

    @Override
    public ConnectionProtocol getProtocol() {
        return type.getConnectionProtocol();
    }

}
