package me.earth.pingbypass.server.session;

import com.mojang.authlib.GameProfile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import me.earth.pingbypass.api.event.api.Subscriber;
import me.earth.pingbypass.api.ducks.network.IConnection;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.event.PbPacketEvent;
import me.earth.pingbypass.server.handlers.play.S2PB2CPipeline;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.network.CommonListenerCookie;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Session extends Connection implements IConnection {
    private final List<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private final PingBypassServer server;
    private final S2PB2CPipeline pipeline;

    private volatile Integer initialTeleportId = null;
    private CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "Unknown"));
    @Setter(AccessLevel.PACKAGE)
    private boolean primarySession;
    private boolean completed;
    private String userName;
    private boolean admin;
    private UUID uuid;
    private String id;

    public Session(PingBypassServer server) {
        super(PacketFlow.SERVERBOUND);
        this.pipeline = new S2PB2CPipeline(this);
        addSubscriber(this.pipeline);
        this.server = server;
    }

    @Override
    public void pingbypass$send(Packet<?> packet) {
        this.send(packet);
    }

    @Override
    public PacketFlow pingbypass$getReceiving() {
        return this.getSending();
    }

    @Override
    public PacketEvent<?> getSendEvent(Packet<?> packet) {
        return new PbPacketEvent.Pb2C<>(packet, this);
    }

    @Override
    public PacketEvent<?> getPostSendEvent(Packet<?> packet) {
        return new PbPacketEvent.Pb2CPostSend<>(packet, this);
    }

    @Override
    public PacketEvent<?> getPostReceiveEvent(Packet<?> packet) {
        return new PbPacketEvent.C2PbPostReceive<>(packet, this);
    }

    @Override
    public PacketEvent<?> getReceiveEvent(Packet<?> packet) {
        return new PbPacketEvent.C2Pb<>(packet, this);
    }

    @Synchronized("subscribers")
    public void subscribe() {
        if (isConnected()) {
            for (Subscriber subscriber : subscribers) {
                if (!server.getEventBus().isSubscribed(subscriber)) {
                    server.getEventBus().subscribe(subscriber);
                }
            }
        }
    }

    @Synchronized("subscribers")
    public void addSubscriber(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    public void initialTeleport(double x, double y, double z, float yaw, float pitch) {
        send(new ClientboundPlayerPositionPacket(x, y, z, yaw, pitch, Collections.emptySet(), Objects.requireNonNull(initialTeleportId)));
    }

    public void clearInitialTeleportId() {
        initialTeleportId = null;
    }

}
