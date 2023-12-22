package me.earth.pingbypass.api.util;

import me.earth.pingbypass.api.traits.Streamable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

public class ExtendedStreamable<T> implements Streamable<T> {
    private final Streamable<? extends T> delegate;
    private final T[] additional;

    @SafeVarargs
    public ExtendedStreamable(Streamable<? extends T> delegate, T... additional) {
        this.delegate = delegate;
        this.additional = additional;
    }

    @Override
    public Stream<T> stream() {
        return Stream.concat(delegate.stream(), Stream.of(additional));
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

}
