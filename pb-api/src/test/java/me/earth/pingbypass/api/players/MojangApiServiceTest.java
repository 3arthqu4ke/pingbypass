package me.earth.pingbypass.api.players;

import me.earth.pingbypass.api.players.impl.MojangApiService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MojangApiServiceTest {
    @Test
    @Disabled // we do not want to make internet connections every test
    public void testMojangApiService() throws ExecutionException, InterruptedException {
        MojangApiService apiService = new MojangApiService(Executors.newSingleThreadExecutor());
        CompletableFuture<UUID> future = apiService.getUUID("3arthqu4ke");
        UUID uuid = future.get();
        assertEquals(UUID.fromString("8af022c8-b926-41a0-8b79-2b544ff00fcf"), uuid);
    }

}
