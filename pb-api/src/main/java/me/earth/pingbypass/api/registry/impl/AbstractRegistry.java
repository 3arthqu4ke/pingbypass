package me.earth.pingbypass.api.registry.impl;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.registry.GetsByClass;
import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.traits.Nameable;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Registries based on this class are very slow and synchronized when adding elements and take up lots of space.
 * However, iterating and retrieval is very fast.
 *
 * @param <T> the type of element contained in this registry.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRegistry<T extends Nameable> implements Registry<T>, GetsByClass.UsingStream<T> {
    protected final Object lock = new Object();
    protected final Map<String, T> lookupByName = new HashMap<>();
    protected final Collection<T> iteratorAndStreamProvider;

    protected abstract boolean registerSynchronously(T object);

    protected boolean unregisterSynchronously(T object) {
        lookupByName.remove(object.getNameLowerCase());
        return iteratorAndStreamProvider.removeIf(nameable -> nameable.getName().equalsIgnoreCase(object.getName()));
    }

    @Override
    @Synchronized("lock")
    public boolean register(T object) {
        return registerSynchronously(object);
    }

    @Override
    @Synchronized("lock")
    public boolean unregister(T object) {
        return unregisterSynchronously(object);
    }

    @Override
    public Optional<T> getByName(String name) {
        return Optional.ofNullable(lookupByName.get(name.toLowerCase()));
    }

    @Override
    public Stream<T> stream() {
        return iteratorAndStreamProvider.stream();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return iteratorAndStreamProvider.iterator();
    }

    @Override
    public String toString() {
        return "%s(%s)".formatted(
                getClass().getName(),
                stream().map(Nameable::getName).collect(Collectors.joining(",")));
    }

    @Override
    public int size() {
        return lookupByName.size();
    }

}
