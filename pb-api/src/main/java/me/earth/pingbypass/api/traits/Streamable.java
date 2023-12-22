package me.earth.pingbypass.api.traits;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Streamable<T> extends Iterable<T> {
    Streamable<?> EMPTY = new Streamable<>() {
        @Override
        public Stream<Object> stream() {
            return Stream.empty();
        }

        @NotNull
        @Override
        public Iterator<Object> iterator() {
            return Collections.emptyIterator();
        }
    };

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @SuppressWarnings("unchecked")
    static <V extends Nameable> Streamable<V> empty() {
        return (Streamable<V>) EMPTY;
    }

}
