package me.earth.pingbypass.api.util;

import lombok.SneakyThrows;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, EX extends Throwable> extends Consumer<T> {
    void acceptWithException(T t) throws EX;

    @Override
    @SneakyThrows
    default void accept(T t) {
        this.acceptWithException(t);
    }

}
