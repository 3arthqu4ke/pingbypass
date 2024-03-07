package me.earth.pingbypass.server.handlers.play;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.handlers.play.world.ChunkSender;
import me.earth.pingbypass.server.handlers.play.world.EntitySender;
import me.earth.pingbypass.server.handlers.play.world.PlayerInfoUpdatePacketSender;
import me.earth.pingbypass.server.handlers.play.world.ScoreboardSender;
import me.earth.pingbypass.server.mixins.inventory.IAbstractContainerMenu;
import me.earth.pingbypass.server.mixins.network.IClientPacketListener;
import me.earth.pingbypass.server.mixins.player.ILocalPlayer;
import me.earth.pingbypass.server.mixins.stats.IRecipeBook;
import me.earth.pingbypass.server.mixins.world.IBiomeManager;
import me.earth.pingbypass.server.mixins.world.IClientLevelData;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.storage.LevelData;

import java.util.Random;

/**
 * @see net.minecraft.server.players.PlayerList#placeNewPlayer(Connection, ServerPlayer, CommonListenerCookie)
 */
@Slf4j
public class JoinWorldService {
    private final PlayerInfoUpdatePacketSender playerInfoUpdatePacketSender = new PlayerInfoUpdatePacketSender();
    private final ScoreboardSender scoreboardSender = new ScoreboardSender();
    private final EntitySender entitySender = new EntitySender();
    private final ChunkSender chunkSender = new ChunkSender();
    private final Random random = new Random();

    public void join(Session session, LocalPlayer player, MultiPlayerGameMode gameMode, ClientLevel level) {
        int initialTeleportId = random.nextInt(); // we could make it negative with just the random bounds?
        initialTeleportId = initialTeleportId > 0 ? -initialTeleportId : initialTeleportId; // negative as minecraft generally does not use negative ids
        session.setInitialTeleportId(initialTeleportId);

        CompoundTag compoundTag = new CompoundTag();
        player.save(compoundTag);

        String ip = session.getLoggableAddress(session.getServer().getServerConfig().get(ServerConstants.LOG_IPS));
        log.info("{}[{}] logged in with entity id {} at ({}, {}, {})",
                player.getName().getString(), ip, player.getId(), player.getX(), player.getY(), player.getZ());

        LevelData levelData = level.getLevelData();
        GameRules gameRules = level.getGameRules();
        CommonPlayerSpawnInfo spawnInfo = createCommonSpawnInfo(player, gameMode, level);
        session.send(
                new ClientboundLoginPacket(
                        player.getId(),
                        levelData.isHardcore(),
                        player.connection.levels(),
                        session.getServer().getMaxPlayersService().getMaxPlayers(),
                        ((IClientPacketListener) player.connection).getServerChunkRadius(),
                        level.getServerSimulationDistance(),
                        gameRules.getBoolean(GameRules.RULE_REDUCEDDEBUGINFO),
                        !gameRules.getBoolean(GameRules.RULE_DO_IMMEDIATE_RESPAWN),
                        gameRules.getBoolean(GameRules.RULE_LIMITED_CRAFTING),
                        spawnInfo
                )
        );
        // additionally we need to send a respawn packet to clear the current view entity so the player does not fall out of the map
        session.send(new ClientboundRespawnPacket(spawnInfo, (byte) 0)); // I dont think client should keep any attributes

        session.send(new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
        session.send(new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
        session.send(new ClientboundSetCarriedItemPacket(player.getInventory().selected));
        session.send(new ClientboundUpdateRecipesPacket(player.connection.getRecipeManager().getRecipes()));
        session.send(new ClientboundEntityEventPacket(player, (byte) (((ILocalPlayer) player).invokeGetPermissionLevel() + EntityEvent.PERMISSION_LEVEL_ALL)));

        session.send(
                new ClientboundRecipePacket(
                        ClientboundRecipePacket.State.INIT,
                        ((IRecipeBook) player.getRecipeBook()).getKnown(),
                        ((IRecipeBook) player.getRecipeBook()).getHighlight(),
                        player.getRecipeBook().getBookSettings()
                )
        );

        scoreboardSender.updateEntireScoreboard(level.getScoreboard(), session);
        // playerlist broadcasts join message here

        ServerStatus serverStatus = session.getServer().getServerStatusService().getServerStatus();
        if (serverStatus != null) {
            // ServerPlayer.sendServerStatus
            session.send(
                    new ClientboundServerDataPacket(
                            serverStatus.description(),
                            serverStatus.favicon().map(ServerStatus.Favicon::iconBytes),
                            serverStatus.enforcesSecureChat()
                    )
            );
        }

        playerInfoUpdatePacketSender.send(session, new GameProfileTranslation(session), player.connection, player, gameMode, level);

        this.sendLevelInfo(session, level);

        for (MobEffectInstance effect : player.getActiveEffects()) {
            session.send(new ClientboundUpdateMobEffectPacket(player.getId(), effect));
        }

        // I think this should be enough for riding
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            if (vehicle instanceof LivingEntity livingEntity) {
                for (MobEffectInstance effect : livingEntity.getActiveEffects()) {
                    session.send(new ClientboundUpdateMobEffectPacket(vehicle.getId(), effect));
                }
            }
        }

        sendContainer(session, player.inventoryMenu);
        if (player.containerMenu != player.inventoryMenu) {
            Screen screen = session.getServer().getMinecraft().screen;
            Component component = screen == null ? Component.literal("Unknown Container") : screen.getTitle();
            try {
                session.send(new ClientboundOpenScreenPacket(player.containerMenu.containerId, player.containerMenu.getType(), component));
                sendContainer(session, player.containerMenu);
            } catch (UnsupportedOperationException ignored) {
                // containerMenu.getType() might throw this, e.g. for CreativeModeInventoryScreen
            }
        }

        session.send(new ClientboundSetHealthPacket(player.getHealth(), player.getFoodData().getFoodLevel(), player.getFoodData().getSaturationLevel()));
        session.send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
        entitySender.sendEntities(session, level);
        chunkSender.send(session, player, level);
        // TODO: initial teleport, send with id and catch the response!
        // TODO: set motion?!
        session.initialTeleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        session.send(new ClientboundSetEntityMotionPacket(player));
    }

    private CommonPlayerSpawnInfo createCommonSpawnInfo(LocalPlayer player, MultiPlayerGameMode gameMode, ClientLevel level) {
        return new CommonPlayerSpawnInfo(
                level.dimensionTypeId(),
                level.dimension(),
                ((IBiomeManager) level.getBiomeManager()).getBiomeZoomSeed(),
                gameMode.getPlayerMode(),
                gameMode.getPreviousPlayerMode(),
                level.isDebug(),
                ((IClientLevelData) level.getLevelData()).isIsFlat(),
                player.getLastDeathLocation(),
                player.getPortalCooldown()
        );
    }

    private void sendContainer(Session session, AbstractContainerMenu menu) {
        session.send(
                new ClientboundContainerSetContentPacket(
                        menu.containerId,
                        menu.getStateId(),
                        menu.getItems(),
                        menu.getCarried())
        );

        var dataSlots = ((IAbstractContainerMenu) menu).getDataSlots();
        for (int i = 0; i < dataSlots.size(); i++) {
            DataSlot slot = dataSlots.get(i);
            session.send(new ClientboundContainerSetDataPacket(menu.containerId, i, slot.get()));
        }
    }

    private void sendLevelInfo(Session session, ClientLevel level) {
        session.send(new ClientboundInitializeBorderPacket(level.getWorldBorder()));
        session.send(new ClientboundSetTimePacket(level.getGameTime(), level.getDayTime(), level.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        session.send(new ClientboundSetDefaultSpawnPositionPacket(level.getSharedSpawnPos(), level.getSharedSpawnAngle()));

        if (level.isRaining()) {
            session.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
            session.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, level.getRainLevel(1.0F)));
            session.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, level.getThunderLevel(1.0F)));
        }

        session.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.LEVEL_CHUNKS_LOAD_START, 0.0F));

        // this.server.tickRateManager().updateJoiningPlayer(session);
        session.send(new ClientboundTickingStatePacket(level.tickRateManager().tickrate(), level.tickRateManager().isFrozen()));
        session.send(new ClientboundTickingStepPacket(level.tickRateManager().frozenTicksToRun()));
    }

}
