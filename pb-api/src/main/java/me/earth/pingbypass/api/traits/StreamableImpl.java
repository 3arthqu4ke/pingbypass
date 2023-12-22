package me.earth.pingbypass.api.traits;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class StreamableImpl<T> implements Streamable<T> {
    private final Collection<T> backingCollection;

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return backingCollection.iterator();
    }

    @Override
    public Stream<T> stream() {
        return backingCollection.stream();
    }

    @SafeVarargs
    public static <V> Streamable<V> of(V... args) {
        return new StreamableImpl<>(Arrays.asList(args));
    }

}
