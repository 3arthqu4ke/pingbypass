package me.earth.pingbypass.api.util.exceptions;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;

/**
 * Does all the {@code mc.player != null} checks for you and binds them to thread-safe parameters.
 * @see me.earth.pingbypass.api.event.SafeListener
 */
@UtilityClass
public class NullabilityUtil {
    public static void safe(Minecraft mc, PlayerLevelAndGameModeConsumer action) {
        safeOr(mc, action, () -> {});
    }

    public static void safeOr(Minecraft mc, PlayerLevelAndGameModeConsumer action, Runnable or) {
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;
        MultiPlayerGameMode gameMode = mc.gameMode;
        if (level != null && player != null && gameMode != null) {
            action.accept(player, level, gameMode);
        } else {
            or.run();
        }
    }

    @FunctionalInterface
    public interface PlayerLevelAndGameModeConsumer {
        /**
         * @param player {@link Minecraft#player}
         * @param level {@link Minecraft#level}
         * @param gameMode {@link Minecraft#gameMode}
         */
        void accept(LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode);
    }

}
