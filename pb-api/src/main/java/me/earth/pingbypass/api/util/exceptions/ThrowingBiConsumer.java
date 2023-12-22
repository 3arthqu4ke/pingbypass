package me.earth.pingbypass.api.util.exceptions;

import lombok.SneakyThrows;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U, EX extends Throwable> extends BiConsumer<T, U> {
    void acceptWithException(T t, U u) throws EX;

    @Override
    @SneakyThrows
    default void accept(T t, U u) {
        this.acceptWithException(t, u);
    }

}
