package me.earth.pingbypass.api.registry.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConflictSolution<T> {
    private static final ConflictSolution<?> SURRENDER = new ConflictSolution<>(Action.SURRENDER, null);
    private final Action action;
    private final @Nullable T value;

    @SuppressWarnings("unchecked")
    public static <V> ConflictSolution<V> surrender() {
        return (ConflictSolution<V>) SURRENDER;
    }

    public static <V> ConflictSolution<V> replace(@NotNull V with) {
        return new ConflictSolution<>(Action.REPLACE, with);
    }

    public static <V> ConflictSolution<V> next(@NotNull V next) {
        return new ConflictSolution<>(Action.NEXT, next);
    }

    public enum Action {
        SURRENDER,
        REPLACE,
        NEXT
    }

}
