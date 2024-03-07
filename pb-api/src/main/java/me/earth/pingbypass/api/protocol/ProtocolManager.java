package me.earth.pingbypass.api.protocol;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.event.SubscriberImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ProtocolManager extends SubscriberImpl {
    public static final DiscardedPayload PAYLOAD = new DiscardedPayload(new ResourceLocation(Constants.NAME_LOW, "processed"));

    private final Map<ResourceLocation, PacketHandler<?>> factories = new ConcurrentHashMap<>();
    private final PingBypass pingBypass;

    public <T extends PBPacket> void register(ResourceLocation location, FriendlyByteBuf.Reader<T> factory) {
        register(location, factory, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends PBPacket> void register(ResourceLocation location, FriendlyByteBuf.Reader<? extends T> factory, @Nullable ProtocolHandler<? extends T> handler) {
        factories.put(location, new PacketHandler<>((FriendlyByteBuf.Reader<T>) factory, (ProtocolHandler<T>) handler));
    }

    public @Nullable PBPacket handle(ResourceLocation location, FriendlyByteBuf buf) {
        PacketHandler<?> handler = factories.get(location);
        if (handler != null) {
            return handler.factory.apply(buf);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends PBPacket> boolean handle(Connection connection, T packet) {
        PacketHandler<T> handler = (PacketHandler<T>) factories.get(packet.getId());
        if (handler != null) {
            handler.handle(packet, pingBypass, connection);
            return true;
        }

        return false;
    }

    private record PacketHandler<T extends PBPacket>(FriendlyByteBuf.Reader<T> factory, @Nullable ProtocolHandler<T> handler) {
        public void handle(T packet, PingBypass pingBypass, Connection connection) {
            if (handler != null) {
                handler.handle(packet, pingBypass, connection);
            } else if (packet instanceof ProtocolHandler.SelfHandling selfHandling) {
                selfHandling.handle(pingBypass, connection);
            }
        }
    }

}
