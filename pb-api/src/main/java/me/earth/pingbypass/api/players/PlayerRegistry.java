package me.earth.pingbypass.api.players;

import me.earth.pingbypass.api.registry.Registry;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRegistry extends Registry<PlayerInfo> {
    Optional<PlayerInfo> getByUUID(UUID uuid);

    boolean contains(UUID uuid);

    default boolean contains(String name) {
        return getByName(name).isPresent();
    }

}
