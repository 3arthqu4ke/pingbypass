package me.earth.pingbypass.util.packets;

import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientAdvancementTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCraftingBookDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientEnchantItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientPrepareCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSpectatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerBoatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientSteerVehiclePacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementTabPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerAdvancementsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerCombatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerSetCooldownPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerUnlockRecipesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerPreparedCraftingGridPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import com.github.steveice10.mc.protocol.packet.login.client.EncryptionResponsePacket;
import com.github.steveice10.mc.protocol.packet.login.client.LoginStartPacket;
import com.github.steveice10.mc.protocol.packet.login.server.EncryptionRequestPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginSuccessPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusPingPacket;
import com.github.steveice10.mc.protocol.packet.status.client.StatusQueryPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusPongPacket;
import com.github.steveice10.mc.protocol.packet.status.server.StatusResponsePacket;
import com.github.steveice10.packetlib.packet.Packet;

import java.util.HashMap;
import java.util.Map;

/**
 * PacketIDs for SteveIce10s MCProtocolLib.
 */
public class LibraryIDs
{
    private static final Map<Class<? extends Packet>, Integer> packets = new HashMap<>();

    public static int getPacketID(Packet packet)
    {
        return getPacketID(packet.getClass());
    }

    public static int getPacketID(Class<? extends Packet> packet)
    {
        return packets.get(packet);
    }

    static
    {
        registerPacket(0x00, HandshakePacket.class);

        registerPacket(0x00, LoginDisconnectPacket.class);
        registerPacket(0x01, EncryptionRequestPacket.class);
        registerPacket(0x02, LoginSuccessPacket.class);
        registerPacket(0x03, LoginSetCompressionPacket.class);

        registerPacket(0x00, LoginStartPacket.class);
        registerPacket(0x01, EncryptionResponsePacket.class);

        registerPacket(0x00, ClientTeleportConfirmPacket.class);
        registerPacket(0x01, ClientTabCompletePacket.class);
        registerPacket(0x02, ClientChatPacket.class);
        registerPacket(0x03, ClientRequestPacket.class);
        registerPacket(0x04, ClientSettingsPacket.class);
        registerPacket(0x05, ClientConfirmTransactionPacket.class);
        registerPacket(0x06, ClientEnchantItemPacket.class);
        registerPacket(0x07, ClientWindowActionPacket.class);
        registerPacket(0x08, ClientCloseWindowPacket.class);
        registerPacket(0x09, ClientPluginMessagePacket.class);
        registerPacket(0x0A, ClientPlayerInteractEntityPacket.class);
        registerPacket(0x0B, ClientKeepAlivePacket.class);
        registerPacket(0x0C, ClientPlayerMovementPacket.class);
        registerPacket(0x0D, ClientPlayerPositionPacket.class);
        registerPacket(0x0E, ClientPlayerPositionRotationPacket.class);
        registerPacket(0x0F, ClientPlayerRotationPacket.class);
        registerPacket(0x10, ClientVehicleMovePacket.class);
        registerPacket(0x11, ClientSteerBoatPacket.class);
        registerPacket(0x12, ClientPrepareCraftingGridPacket.class);
        registerPacket(0x13, ClientPlayerAbilitiesPacket.class);
        registerPacket(0x14, ClientPlayerActionPacket.class);
        registerPacket(0x15, ClientPlayerStatePacket.class);
        registerPacket(0x16, ClientSteerVehiclePacket.class);
        registerPacket(0x17, ClientCraftingBookDataPacket.class);
        registerPacket(0x18, ClientResourcePackStatusPacket.class);
        registerPacket(0x19, ClientAdvancementTabPacket.class);
        registerPacket(0x1A, ClientPlayerChangeHeldItemPacket.class);
        registerPacket(0x1B, ClientCreativeInventoryActionPacket.class);
        registerPacket(0x1C, ClientUpdateSignPacket.class);
        registerPacket(0x1D, ClientPlayerSwingArmPacket.class);
        registerPacket(0x1E, ClientSpectatePacket.class);
        registerPacket(0x1F, ClientPlayerPlaceBlockPacket.class);
        registerPacket(0x20, ClientPlayerUseItemPacket.class);

        registerPacket(0x00, ServerSpawnObjectPacket.class);
        registerPacket(0x01, ServerSpawnExpOrbPacket.class);
        registerPacket(0x02, ServerSpawnGlobalEntityPacket.class);
        registerPacket(0x03, ServerSpawnMobPacket.class);
        registerPacket(0x04, ServerSpawnPaintingPacket.class);
        registerPacket(0x05, ServerSpawnPlayerPacket.class);
        registerPacket(0x06, ServerEntityAnimationPacket.class);
        registerPacket(0x07, ServerStatisticsPacket.class);
        registerPacket(0x08, ServerBlockBreakAnimPacket.class);
        registerPacket(0x09, ServerUpdateTileEntityPacket.class);
        registerPacket(0x0A, ServerBlockValuePacket.class);
        registerPacket(0x0B, ServerBlockChangePacket.class);
        registerPacket(0x0C, ServerBossBarPacket.class);
        registerPacket(0x0D, ServerDifficultyPacket.class);
        registerPacket(0x0E, ServerTabCompletePacket.class);
        registerPacket(0x0F, ServerChatPacket.class);
        registerPacket(0x10, ServerMultiBlockChangePacket.class);
        registerPacket(0x11, ServerConfirmTransactionPacket.class);
        registerPacket(0x12, ServerCloseWindowPacket.class);
        registerPacket(0x13, ServerOpenWindowPacket.class);
        registerPacket(0x14, ServerWindowItemsPacket.class);
        registerPacket(0x15, ServerWindowPropertyPacket.class);
        registerPacket(0x16, ServerSetSlotPacket.class);
        registerPacket(0x17, ServerSetCooldownPacket.class);
        registerPacket(0x18, ServerPluginMessagePacket.class);
        registerPacket(0x19, ServerPlaySoundPacket.class);
        registerPacket(0x1A, ServerDisconnectPacket.class);
        registerPacket(0x1B, ServerEntityStatusPacket.class);
        registerPacket(0x1C, ServerExplosionPacket.class);
        registerPacket(0x1D, ServerUnloadChunkPacket.class);
        registerPacket(0x1E, ServerNotifyClientPacket.class);
        registerPacket(0x1F, ServerKeepAlivePacket.class);
        registerPacket(0x20, ServerChunkDataPacket.class);
        registerPacket(0x21, ServerPlayEffectPacket.class);
        registerPacket(0x22, ServerSpawnParticlePacket.class);
        registerPacket(0x23, ServerJoinGamePacket.class);
        registerPacket(0x24, ServerMapDataPacket.class);
        registerPacket(0x25, ServerEntityMovementPacket.class);
        registerPacket(0x26, ServerEntityPositionPacket.class);
        registerPacket(0x27, ServerEntityPositionRotationPacket.class);
        registerPacket(0x28, ServerEntityRotationPacket.class);
        registerPacket(0x29, ServerVehicleMovePacket.class);
        registerPacket(0x2A, ServerOpenTileEntityEditorPacket.class);
        registerPacket(0x2B, ServerPreparedCraftingGridPacket.class);
        registerPacket(0x2C, ServerPlayerAbilitiesPacket.class);
        registerPacket(0x2D, ServerCombatPacket.class);
        registerPacket(0x2E, ServerPlayerListEntryPacket.class);
        registerPacket(0x2F, ServerPlayerPositionRotationPacket.class);
        registerPacket(0x30, ServerPlayerUseBedPacket.class);
        registerPacket(0x31, ServerUnlockRecipesPacket.class);
        registerPacket(0x32, ServerEntityDestroyPacket.class);
        registerPacket(0x33, ServerEntityRemoveEffectPacket.class);
        registerPacket(0x34, ServerResourcePackSendPacket.class);
        registerPacket(0x35, ServerRespawnPacket.class);
        registerPacket(0x36, ServerEntityHeadLookPacket.class);
        registerPacket(0x37, ServerAdvancementTabPacket.class);
        registerPacket(0x38, ServerWorldBorderPacket.class);
        registerPacket(0x39, ServerSwitchCameraPacket.class);
        registerPacket(0x3A, ServerPlayerChangeHeldItemPacket.class);
        registerPacket(0x3B, ServerDisplayScoreboardPacket.class);
        registerPacket(0x3C, ServerEntityMetadataPacket.class);
        registerPacket(0x3D, ServerEntityAttachPacket.class);
        registerPacket(0x3E, ServerEntityVelocityPacket.class);
        registerPacket(0x3F, ServerEntityEquipmentPacket.class);
        registerPacket(0x40, ServerPlayerSetExperiencePacket.class);
        registerPacket(0x41, ServerPlayerHealthPacket.class);
        registerPacket(0x42, ServerScoreboardObjectivePacket.class);
        registerPacket(0x43, ServerEntitySetPassengersPacket.class);
        registerPacket(0x44, ServerTeamPacket.class);
        registerPacket(0x45, ServerUpdateScorePacket.class);
        registerPacket(0x46, ServerSpawnPositionPacket.class);
        registerPacket(0x47, ServerUpdateTimePacket.class);
        registerPacket(0x48, ServerTitlePacket.class);
        registerPacket(0x49, ServerPlayBuiltinSoundPacket.class);
        registerPacket(0x4A, ServerPlayerListDataPacket.class);
        registerPacket(0x4B, ServerEntityCollectItemPacket.class);
        registerPacket(0x4C, ServerEntityTeleportPacket.class);
        registerPacket(0x4D, ServerAdvancementsPacket.class);
        registerPacket(0x4E, ServerEntityPropertiesPacket.class);
        registerPacket(0x4F, ServerEntityEffectPacket.class);

        registerPacket(0x00, StatusResponsePacket.class);
        registerPacket(0x01, StatusPongPacket.class);

        registerPacket(0x00, StatusQueryPacket.class);
        registerPacket(0x01, StatusPingPacket.class);
    }

    private static void registerPacket(int id, Class<? extends Packet> packet)
    {
        packets.put(packet, id);
    }

}
