package me.earth.pingbypass.api.players.impl;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

// TODO: should this even be part of the API?
@Slf4j
@RequiredArgsConstructor
public class MojangApiService {
    private final ExecutorService executor;

    public CompletableFuture<UUID> getUUID(String name) {
        return CompletableFuture.supplyAsync(() -> getUUIDExceptionally(name), executor);
    }

    @SneakyThrows
    private UUID getUUIDExceptionally(String name) {
        try (var br = new BufferedReader(new InputStreamReader(
                new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()))) {
            String uuid = JsonParser.parseReader(br)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString()
                    .replaceAll("\"", "")
                    .replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

            return UUID.fromString(uuid);
        } catch (JsonParseException | IOException e) {
            log.error("Failed to get UUID for name " + name, e);
            throw e;
        }
    }

}
