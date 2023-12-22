package me.earth.pingbypass.api.players;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// TODO: should this even be part of the API?
public interface UUIDLookupService {
    CompletableFuture<UUID> getUuid(String name);

}
