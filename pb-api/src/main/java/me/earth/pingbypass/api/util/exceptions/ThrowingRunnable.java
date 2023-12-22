package me.earth.pingbypass.api.util.exceptions;

import lombok.SneakyThrows;

@FunctionalInterface
public interface ThrowingRunnable<EX extends Throwable> extends Runnable {
    void runWithException() throws EX;

    @Override
    @SneakyThrows
    default void run() {
        this.runWithException();
    }

}
