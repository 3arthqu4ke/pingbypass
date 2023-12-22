package me.earth.pingbypass.server.service;

public record LegacyServerStatus(String serverVersion, String motd,
                                 int players, int maxPlayers) {
}
