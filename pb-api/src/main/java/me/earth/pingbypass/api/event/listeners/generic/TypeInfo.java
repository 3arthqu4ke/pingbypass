package me.earth.pingbypass.api.event.listeners.generic;

public record TypeInfo<T>(Class<T> type, Class<?> generic) {
}
