package me.earth.pingbypass.api.protocol;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.util.exceptions.NullabilityUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;

public interface ProtocolHandler<T extends Packet<?>> {
    default void handle(T packet, PingBypass pingBypass, Connection connection) {
        NullabilityUtil.safe(pingBypass.getMinecraft(), ((player, level, gameMode) -> handle(packet, pingBypass, connection, player, level, gameMode)));
    }

    default void handle(T packet, PingBypass pingBypass, Connection connection, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode) {

    }

    interface SelfHandling {
        default void handle(PingBypass pingBypass, Connection connection) {
            NullabilityUtil.safe(pingBypass.getMinecraft(), ((player, level, gameMode) -> handle(pingBypass, connection, player, level, gameMode)));
        }

        default void handle(PingBypass pingBypass, Connection connection, LocalPlayer player, ClientLevel level, MultiPlayerGameMode gameMode) {

        }
    }

}
