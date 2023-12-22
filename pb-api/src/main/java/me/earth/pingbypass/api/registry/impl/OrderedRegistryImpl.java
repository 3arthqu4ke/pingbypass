package me.earth.pingbypass.api.registry.impl;


import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.registry.OrderedRegistry;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class OrderedRegistryImpl<T extends Nameable> extends AbstractConflictSolvingRegistry<T>
        implements OrderedRegistry<T> {
    private final List<T> list;

    public OrderedRegistryImpl(RegistryConflictSolver<T> conflictSolver) {
        super(new CopyOnWriteArrayList<>(), conflictSolver);
        this.list = (List<T>) iteratorAndStreamProvider;
    }

    public OrderedRegistryImpl() {
        this(RegistryConflictSolver.none());
    }

    @Override
    public boolean registerSynchronously(T object) {
        return registerAt(-1, object);
    }

    @Override
    public boolean registerBefore(T object, T before) {
        return registerAt(indexOf(before), object);
    }

    @Override
    public boolean registerAfter(T object, T after) {
        int indexOfAfter = indexOf(after);
        return registerAt(indexOfAfter < 0 ? -1 : indexOfAfter + 1, object);
    }

    @Override
    public boolean registerFirst(T object) {
        return registerAt(0, object);
    }

    private boolean registerAt(int index, T object) {
        Optional<T> solvedConflict = solveConflict(object);
        if (solvedConflict.isEmpty()) {
            return false;
        }

        this.unregister(object);
        lookupByName.put(object.getNameLowerCase(), object);
        if (index < 0) {
            list.add(object);
        } else {
            list.add(index, object);
        }

        return true;
    }

    private int indexOf(T object) {
        return list.indexOf(object);
    }

}
