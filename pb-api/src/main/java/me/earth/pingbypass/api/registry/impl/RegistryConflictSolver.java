package me.earth.pingbypass.api.registry.impl;

import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.traits.Nameable;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RegistryConflictSolver<T extends Nameable> {
    RegistryConflictSolver<?> NONE = (reg,t1,t2) -> ConflictSolution.surrender();

    ConflictSolution<T> solveConflict(Registry<T> registry, @Nullable T name, T registering);

    @SuppressWarnings("unchecked")
    static <V extends Nameable> RegistryConflictSolver<V> none() {
        return (RegistryConflictSolver<V>) NONE;
    }

}
