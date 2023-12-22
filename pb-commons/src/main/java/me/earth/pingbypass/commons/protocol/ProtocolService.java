package me.earth.pingbypass.commons.protocol;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ProtocolService {
    private final Map<Integer, Function<FriendlyByteBuf, PbPacket<?>>> factories = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends PacketListener> void handle(FriendlyByteBuf buf, T handler) throws ProtocolException {
        if (!buf.isReadable(4)) {
            throw new ProtocolException("Cannot read id from %s, %s".formatted(buf, handler));
        }

        int id = buf.readInt();
        Function<FriendlyByteBuf, PbPacket<?>> factory = factories.get(id);
        if (factory == null) {
            throw new ProtocolException("Could not find a packet for id %d! %s, %s".formatted(id, buf, handler));
        }

        PbPacket<T> packet = (PbPacket<T>) factory.apply(buf);
        packet.process(handler);
    }

    public void register(int id, Function<FriendlyByteBuf, PbPacket<?>> factory) {
        factories.put(id, factory);
    }

}
