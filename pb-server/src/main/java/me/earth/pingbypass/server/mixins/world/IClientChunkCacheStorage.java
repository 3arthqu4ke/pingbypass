package me.earth.pingbypass.server.mixins.world;

import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @see net.minecraft.client.multiplayer.ClientChunkCache
 */
@Mixin(targets = "net.minecraft.client.multiplayer.ClientChunkCache$Storage")
public interface IClientChunkCacheStorage {
    @Invoker("getIndex")
    int invokeGetIndex(int x, int z);

    @Accessor("chunks")
    AtomicReferenceArray<LevelChunk> getChunks();

    @Accessor("chunkRadius")
    int getChunkRadius();

    @Accessor("viewCenterX")
    int getViewCenterX();

    @Accessor("viewCenterZ")
    int getViewCenterZ();

}
