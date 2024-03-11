package me.earth.pingbypass.server.session;

import com.mojang.authlib.GameProfile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.earth.pingbypass.api.ducks.network.IConnection;
import me.earth.pingbypass.api.event.network.PacketEvent;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.event.PbPacketEvent;
import me.earth.pingbypass.server.handlers.play.GameProfileTranslation;
import me.earth.pingbypass.server.handlers.play.PlayerUpdateHandler;
import me.earth.pingbypass.server.handlers.play.S2PB2CPipeline;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.network.CommonListenerCookie;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class Session extends Connection implements IConnection {
    private final PlayerUpdateHandler playerUpdateHandler = new PlayerUpdateHandler();
    private final GameProfileTranslation gameProfileTranslation;
    private final SessionSubscriberService subscriberService;
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
        this.gameProfileTranslation = new GameProfileTranslation(this);
        this.subscriberService = new SessionSubscriberService(server.getEventBus());
        this.pipeline = new S2PB2CPipeline(this);
        this.server = server;
    }

    public void whenMadePrimarySession() {
        subscriberService.addAndSubscribe(playerUpdateHandler);
        subscriberService.addAndSubscribe(gameProfileTranslation);
        subscriberService.addAndSubscribe(pipeline);
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

    public void initialTeleport(double x, double y, double z, float yaw, float pitch) {
        send(new ClientboundPlayerPositionPacket(x, y, z, yaw, pitch, Collections.emptySet(), Objects.requireNonNull(initialTeleportId)));
    }

    public void clearInitialTeleportId() {
        initialTeleportId = null;
    }

}
