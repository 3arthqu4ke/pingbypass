package me.earth.pingbypass.api.registry.impl;

import me.earth.pingbypass.api.traits.Nameable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class SortedRegistry<T extends Nameable> extends AbstractConflictSolvingRegistry<T> {
    private final Comparator<T> comparator;
    private final List<T> backingList;

    public SortedRegistry(RegistryConflictSolver<T> conflictSolver, Comparator<T> comparator) {
        super(new CopyOnWriteArrayList<>(), conflictSolver);
        this.comparator = comparator;
        this.backingList = ((List<T>) iteratorAndStreamProvider);
    }

    public SortedRegistry(RegistryConflictSolver<T> conflictSolver) {
        this(conflictSolver, Nameable::compareAlphabetically);
    }

    public SortedRegistry() {
        this(RegistryConflictSolver.none());
    }

    @Override
    protected boolean registerSynchronously(T object) {
        Optional<T> solvedConflict = solveConflict(object);
        if (solvedConflict.isEmpty()) {
            return false;
        }

        this.unregister(object);
        lookupByName.put(object.getNameLowerCase(), object);
        insertIntoSortedList(backingList, object, comparator);
        return true;
    }

    private static <T> void insertIntoSortedList(List<T> list, T element, Comparator<T> comparator) {
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (comparator.compare(t, element) >= 0) {
                list.add(i, element);
                return;
            }
        }

        list.add(element);
    }

}
