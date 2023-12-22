package me.earth.pingbypass.api.registry.impl;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.traits.Nameable;

@Slf4j
@UtilityClass
public class ConflictSolvers {
    public static <T extends Nameable> RegistryConflictSolver<T> log() {
        return (reg,t1,t2) -> {
            log.error("Failed to register %s on registry %s. Conflict: %s".formatted(t2, reg, t1));
            return ConflictSolution.surrender();
        };
    }

    public static <T extends Nameable> RegistryConflictSolver<T> error() {
        return (reg,t1,t2) -> {
            throw new IllegalStateException(
                "Failed to register %s on registry %s. Conflict: %s".formatted(t2, reg, t1));
        };
    }

    public static <T extends Nameable> RegistryConflictSolver<T> replace() {
        return (reg,t1,t2) -> {
            log.warn("Overwriting %s %s by %s on registry %s".formatted(t2.getName(), t1, t2, reg));
            return ConflictSolution.replace(t2);
        };
    }

}
