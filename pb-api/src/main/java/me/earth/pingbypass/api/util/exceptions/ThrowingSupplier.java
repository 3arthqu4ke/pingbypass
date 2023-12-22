package me.earth.pingbypass.api.util.exceptions;

import lombok.SneakyThrows;

import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T, EX extends Throwable> extends Supplier<T> {
    T getWithException() throws EX;

    @Override
    @SneakyThrows
    default T get() {
        return getWithException();
    }

}
