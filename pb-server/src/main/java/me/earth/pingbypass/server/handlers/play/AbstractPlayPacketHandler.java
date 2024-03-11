package me.earth.pingbypass.server.handlers.play;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.handlers.AbstractCommonPacketListener;
import me.earth.pingbypass.server.handlers.IServerGamePacketListener;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.server.network.CommonListenerCookie;
import org.jetbrains.annotations.NotNull;

@Slf4j
public abstract class AbstractPlayPacketHandler extends AbstractCommonPacketListener implements IServerGamePacketListener {
    public AbstractPlayPacketHandler(PingBypassServer server, Session session, CommonListenerCookie cookie) {
        super(server, session, cookie, server.getMinecraft());
    }

    @Override
    public void onPacket(Packet<?> packet) {
        LocalPlayer player = mc.player;
        if (player != null) {
            player.connection.send(packet);
        }
    }

    @Override
    public void handlePingRequest(ServerboundPingRequestPacket packet) {
        send(new ClientboundPongResponsePacket(packet.getTime()));
    }

    @Override
    public void handleAnimate(ServerboundSwingPacket packet) {
        IServerGamePacketListener.super.handleAnimate(packet);
        scheduleSafely(((player, level, gameMode) -> player.swing(packet.getHand(), false)));
    }

    @Override
    public void handleContainerClose(ServerboundContainerClosePacket packet) {
        IServerGamePacketListener.super.handleContainerClose(packet);
        scheduleSafely(((player, level, gameMode) -> player.clientSideCloseContainer()));
    }

    @Override
    public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) {
        IServerGamePacketListener.super.handlePlayerAbilities(packet);
        scheduleSafely(((player, level, gameMode) -> player.getAbilities().flying = packet.isFlying() && player.getAbilities().mayfly));
    }

    @Override
    public void handlePlayerAction(ServerboundPlayerActionPacket packet) {
        IServerGamePacketListener.super.handlePlayerAction(packet);
        // TODO: SpeedMine!
        // TODO: Swap Item?
    }

    @Override
    public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) {
        IServerGamePacketListener.super.handlePlayerCommand(packet);
        // TODO: Sneaking etc!!
    }

    @Override
    public void handlePlayerInput(ServerboundPlayerInputPacket packet) {
        IServerGamePacketListener.super.handlePlayerInput(packet);
        // TODO: this.connection.send(new ServerboundPlayerInputPacket(this.xxa, this.zza, this.input.jumping, this.input.shiftKeyDown));
    }

    @Override
    public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) {
        IServerGamePacketListener.super.handleSetCarriedItem(packet);
        // TODO: port Phobot inventory context?
        // TODO: verify, it could be that on the PingBypassServer we have already sent this to the server!
        scheduleSafely(((player, level, gameMode) -> player.getInventory().selected = packet.getSlot()));
    }

    @Override
    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) {
        IServerGamePacketListener.super.handleSetCreativeModeSlot(packet);
        // TODO: set Item?!
    }

    @Override
    public void handleUseItem(ServerboundUseItemPacket packet) {
        IServerGamePacketListener.super.handleUseItem(packet);
        // TODO: set hand?
    }

    @Override
    public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) {
        IServerGamePacketListener.super.handleMoveVehicle(packet);
        // TODO: handle, post event?!
    }

    @Override
    public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) {
        // IServerGamePacketListener.super.handleAcceptTeleportPacket(packet);
        // TODO: cancel? and replace with our own teleportation mechanics?
    }

    @Override
    public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) {
        // IServerGamePacketListener.super.handleRecipeBookSeenRecipePacket(packet);
        // TODO: handle?
    }

    @Override
    public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) {
        IServerGamePacketListener.super.handleRecipeBookChangeSettingsPacket(packet);
        // TODO: handle?
    }

    @Override
    public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) {
        IServerGamePacketListener.super.handleCustomCommandSuggestions(packet);
        // TODO: deploy own command system?
    }

    @Override
    public void handleMovePlayer(ServerboundMovePlayerPacket packet) {
        // IServerGamePacketListener.super.handleMovePlayer(packet);
        scheduleSafely((player, level, gameMode) -> {
            player.setPos(packet.getX(player.getX()), packet.getY(player.getY()), packet.getZ(player.getZ()));
            player.setYRot(packet.getYRot(player.getYRot()));
            player.setXRot(packet.getXRot(player.getXRot()));
            player.setOnGround(packet.isOnGround());
            session.getPlayerUpdateHandler().update(player);
        });
        // TODO: handle! Fire event!
    }

    @Override
    public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) {
        IServerGamePacketListener.super.handleContainerButtonClick(packet);
        // TODO: handle!
    }

    @Override
    public void handleContainerClick(ServerboundContainerClickPacket packet) {
        IServerGamePacketListener.super.handleContainerClick(packet);
        // TODO: handle!
    }

    @Override
    public void onDisconnect(@NotNull Component component) {
        log.info("Session disconnected {} {}", session, component);
    }

}
