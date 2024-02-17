package me.earth.pingbypass.server.handlers.play.world;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.server.mixins.world.IClientChunkCacheStorage;
import me.earth.pingbypass.server.session.Session;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
import net.minecraft.world.level.chunk.LevelChunk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

// TODO: can we do this more asynchronously, instead of sending all chunks?
@Slf4j
public class ChunkSender {
    // TODO: should we use a net.minecraft.server.network.PlayerChunkSender with batching instead?
    public void send(Session session, LocalPlayer player, ClientLevel level) {
        // Minecraft does not really expose a way to get all loaded chunks on the client.
        // The only thing that comes close is ClientChunkCache.Storage.dumpChunks which this method recreates
        ClientChunkCache chunkCache = level.getChunkSource();
        IClientChunkCacheStorage storage = getClientChunkCacheStorage(chunkCache);
        session.send(new ClientboundSetChunkCacheCenterPacket(storage.getViewCenterX(), storage.getViewCenterZ()));
        LevelChunk firstChunk = level.getChunkAt(player.getOnPos());
        session.send(new ClientboundLevelChunkWithLightPacket(firstChunk, level.getLightEngine(), null, null));
        int radius = storage.getChunkRadius();
        for (int z = storage.getViewCenterZ() - radius; z <= storage.getViewCenterZ() + radius; ++z) {
            for (int x = storage.getViewCenterX() - radius; x <= storage.getViewCenterX() + radius; ++x) {
                LevelChunk chunk = storage.getChunks().get(storage.invokeGetIndex(x, z));
                if (chunk != null && chunk != firstChunk) {
                    session.send(new ClientboundLevelChunkWithLightPacket(chunk, level.getLightEngine(), null, null));
                }
            }
        }
    }

    // this accesses ClientChunkCache.storage
    // sadly Mixin does not support @Shadow, or @Accessor for fields with inaccessible types yet :(
    // https://github.com/SpongePowered/Mixin/issues/284
    @SneakyThrows
    private static IClientChunkCacheStorage getClientChunkCacheStorage(ClientChunkCache chunkCache) {
        for (Field field : ClientChunkCache.class.getDeclaredFields()) {
            if (Modifier.isVolatile(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())
                    && IClientChunkCacheStorage.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                return (IClientChunkCacheStorage) Objects.requireNonNull(field.get(chunkCache), "IClientChunkCacheStorage in " + chunkCache + " was null!");
            }
        }

        throw new IllegalStateException("Failed to find IClientChunkCacheStorage in ClientChunkCache " + chunkCache);
    }

}
