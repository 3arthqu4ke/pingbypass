package me.earth.pingbypass.api.registry.impl;

import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.Optional;

// TODO: maybe instead provide class to wrap an existing registry and dont make registries bother with conflict solving?
public interface ConflictSolvingRegistry<T extends Nameable> extends Registry<T> {
    RegistryConflictSolver<T> getConflictSolver();

    default Optional<T> solveConflict(T object) {
        T toAdd = object;
        Optional<T> byName = getByName(toAdd.getName());
        while (byName.isPresent()) {
            ConflictSolution<T> solution = getConflictSolver().solveConflict(this, byName.get(), toAdd);
            switch (solution.getAction()) {
                case SURRENDER -> {
                    return Optional.empty();
                }
                case REPLACE -> {
                    return Optional.of(toAdd);
                }
                case NEXT -> {
                    if (solution.getValue() == null) {
                        throw new NullPointerException("ConflictSolver returned NEXT and null!");
                    }

                    toAdd = solution.getValue();
                    byName = getByName(toAdd.getName());
                }
                default -> throw new IllegalArgumentException("Unknown action " + solution.getAction());
            }
        }

        return Optional.of(toAdd);
    }

}
