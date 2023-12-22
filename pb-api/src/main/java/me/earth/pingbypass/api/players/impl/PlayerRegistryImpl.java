package me.earth.pingbypass.api.players.impl;

import me.earth.pingbypass.api.players.PlayerInfo;
import me.earth.pingbypass.api.players.PlayerRegistry;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerRegistryImpl extends SortedRegistry<PlayerInfo> implements PlayerRegistry {
    private final Map<UUID, PlayerInfo> lookupByUUID = new ConcurrentHashMap<>();

    @Override
    protected boolean registerSynchronously(PlayerInfo playerInfo) {
        unregisterAllInfosWithTheSameUUID(playerInfo);
        if (super.registerSynchronously(playerInfo)) {
            lookupByUUID.put(playerInfo.uuid(), playerInfo);
            return true;
        }

        return false;
    }

    @Override
    protected boolean unregisterSynchronously(PlayerInfo playerInfo) {
        lookupByUUID.remove(playerInfo.uuid());
        boolean result = super.unregisterSynchronously(playerInfo);
        unregisterAllInfosWithTheSameUUID(playerInfo);
        return result;
    }

    @Override
    public boolean contains(UUID uuid) {
        return lookupByUUID.containsKey(uuid);
    }

    @Override
    public Optional<PlayerInfo> getByUUID(UUID uuid) {
        return Optional.ofNullable(lookupByUUID.get(uuid));
    }

    private void unregisterAllInfosWithTheSameUUID(PlayerInfo playerInfo) {
        this.stream().filter(pInfo -> this.contains(playerInfo.uuid())).forEach(this::unregister);
    }

}
