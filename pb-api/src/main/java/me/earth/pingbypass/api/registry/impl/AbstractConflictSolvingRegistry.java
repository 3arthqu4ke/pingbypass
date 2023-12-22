package me.earth.pingbypass.api.registry.impl;

import lombok.Getter;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.Collection;

@Getter
public abstract class AbstractConflictSolvingRegistry<T extends Nameable>
        extends AbstractRegistry<T> implements ConflictSolvingRegistry<T> {
    private final RegistryConflictSolver<T> conflictSolver;

    public AbstractConflictSolvingRegistry(Collection<T> iteratorAndStreamProvider,
                                           RegistryConflictSolver<T> conflictSolver) {
        super(iteratorAndStreamProvider);
        this.conflictSolver = conflictSolver;
    }

}
