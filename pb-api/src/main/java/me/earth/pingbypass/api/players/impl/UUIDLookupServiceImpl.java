package me.earth.pingbypass.api.players.impl;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.players.UUIDLookupService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

// TODO: should this even be part of the API?
@RequiredArgsConstructor
public class UUIDLookupServiceImpl implements UUIDLookupService {
    private final Map<String, UUID> cache = new ConcurrentHashMap<>();
    private final MojangApiService apiService;
    private final Minecraft mc;

    @Override
    public CompletableFuture<UUID> getUuid(String name) {
        UUID result = cache.get(name.toLowerCase());
        if (result != null) {
            return CompletableFuture.completedFuture(result);
        }

        ClientPacketListener connection = mc.getConnection();
        if (connection != null) {
            try {
                result = connection.getOnlinePlayers()
                        .stream()
                        .filter(info -> name.equalsIgnoreCase(info.getProfile().getName()))
                        .map(info -> info.getProfile().getId())
                        .findFirst()
                        .orElse(null);
            } catch (ConcurrentModificationException ignored) {
                // this might get called asynchronously, better be safe
            }

            if (result != null) {
                cache.put(name.toLowerCase(), result);
                return CompletableFuture.completedFuture(result);
            }
        }

        return apiService.getUUID(name).thenApply(uuid -> {
            cache.put(name.toLowerCase(), uuid);
            return uuid;
        });
    }

}
