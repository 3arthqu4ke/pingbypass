package me.earth.pingbypass.server.handlers;

import com.mojang.authlib.GameProfile;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.handlers.configuration.ConfigWorld;
import me.earth.pingbypass.server.handlers.configuration.ConfigWorldPacketHandler;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.*;
import net.minecraft.network.protocol.common.custom.BrandPayload;
import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.config.JoinWorldTask;
import net.minecraft.server.network.config.ServerResourcePackConfigurationTask;
import net.minecraft.tags.TagNetworkSerialization;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.WorldDataConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A custom {@link net.minecraft.server.network.ServerConfigurationPacketListenerImpl}.
 */
@Slf4j
public class ConfigurationPacketListener extends AbstractCommonPacketListener implements ServerCommonPacketListener, IHandler, TickablePacketListener, ServerConfigurationPacketListener {
    private static final Component DISCONNECT_REASON_INVALID_DATA = Component.translatable("multiplayer.disconnect.invalid_player_data");

    private final Queue<ConfigurationTask> configurationTasks = new ConcurrentLinkedQueue<>();
    private final GameProfile gameProfile;

    @Nullable
    private ConfigurationTask currentTask;
    @Nullable
    private ClientInformation clientInformation;

    public ConfigurationPacketListener(PingBypassServer server, Session session, CommonListenerCookie cookie) {
        super(server, session, cookie, server.getMinecraft());
        this.gameProfile = cookie.gameProfile();
    }

    @Override
    protected GameProfile playerProfile() {
        return this.gameProfile;
    }

    @Override
    public void onDisconnect(Component reason) {
        log.info("{} lost connection: {}", this.gameProfile, reason.getString());
        super.onDisconnect(reason);
    }

    public boolean isAcceptingMessages() {
        return session.isConnected();
    }

    public void startConfiguration() {
        this.send(new ClientboundCustomPayloadPacket(new BrandPayload("vanilla")));
        LayeredRegistryAccess<RegistryLayer> registries = server.getWorldStem().registries();
        this.send(new ClientboundUpdateEnabledFeaturesPacket(FeatureFlags.REGISTRY.toNames(WorldDataConfiguration.DEFAULT.enabledFeatures())));
        this.send(new ClientboundRegistryDataPacket(new RegistryAccess.ImmutableRegistryAccess(RegistrySynchronization.networkedRegistries(registries)).freeze()));
        this.send(new ClientboundUpdateTagsPacket(TagNetworkSerialization.serializeTagsToNetwork(registries)));
        this.addOptionalTasks();
        this.configurationTasks.add(new JoinWorldTask());
        this.startNextTask();
    }

    public void returnToWorld() {
        this.configurationTasks.add(new JoinWorldTask());
        this.startNextTask();
    }

    private void addOptionalTasks() {
        //this.server.getServerResourcePack().ifPresent((arg) -> {
        //    this.configurationTasks.add(new ServerResourcePackConfigurationTask(arg));
        //});
    }

    @Override
    public void handleClientInformation(ServerboundClientInformationPacket packet) {
        this.clientInformation = packet.information();
    }

    @Override
    public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
        if (packet.action().isTerminal()) {
            this.finishCurrentTask(ServerResourcePackConfigurationTask.TYPE);
        }
    }

    @Override
    public void handleConfigurationFinished(ServerboundFinishConfigurationPacket packet) {
        session.suspendInboundAfterProtocolChange();
        PacketUtils.ensureRunningOnSameThread(packet, this, this.server.getMinecraft());
        this.finishCurrentTask(JoinWorldTask.TYPE);

        try {
            /* TODO PlayerList lv = this.server.getPlayerList();
            if (lv.getPlayer(this.gameProfile.getId()) != null) {
                this.disconnect(PlayerList.DUPLICATE_LOGIN_DISCONNECT_MESSAGE);
                return;
            }

            Component lv2 = lv.canPlayerLogin(session.getRemoteAddress(), this.gameProfile);
            if (lv2 != null) {
                this.disconnect(lv2);
                return;
            }

            ServerPlayer lv3 = lv.getPlayerForLogin(this.gameProfile, this.clientInformation);
            lv.placeNewPlayer(session, lv3, this.createCookie(this.clientInformation));
            session.resumeInboundAfterProtocolChange();*/
            CommonListenerCookie cookie = this.createCookie(Objects.requireNonNull(this.clientInformation));
            session.setCookie(cookie);
            session.setListener(new ConfigWorldPacketHandler(server, session, cookie));
            new ConfigWorld(server, session).placeNewPlayer(cookie);
            session.resumeInboundAfterProtocolChange();
        } catch (Exception e) {
            log.error("Couldn't place player in world", e);
            server.getSessionManager().disconnect(session, DISCONNECT_REASON_INVALID_DATA);
        }
    }

    private void startNextTask() {
        if (this.currentTask != null) {
            throw new IllegalStateException("Task " + this.currentTask.type().id() + " has not finished yet");
        } else if (this.isAcceptingMessages()) {
            ConfigurationTask configurationTask = this.configurationTasks.poll();
            if (configurationTask != null) {
                this.currentTask = configurationTask;
                configurationTask.start(this::send);
            }
        }
    }

    private void finishCurrentTask(ConfigurationTask.Type taskType) {
        ConfigurationTask.Type type = this.currentTask != null ? this.currentTask.type() : null;
        if (!taskType.equals(type)) {
            throw new IllegalStateException("Unexpected request for task finish, current task: " + type + ", requested: " + taskType);
        } else {
            this.currentTask = null;
            this.startNextTask();
        }
    }

}
