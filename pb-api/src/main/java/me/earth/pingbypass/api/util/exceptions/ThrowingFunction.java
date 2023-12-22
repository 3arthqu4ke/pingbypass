package me.earth.pingbypass.api.util.exceptions;

import lombok.SneakyThrows;

import java.util.function.Function;

@FunctionalInterface
public interface ThrowingFunction<IN, OUT, EX extends Throwable> extends Function<IN, OUT> {
    OUT applyWithException(IN in) throws EX;

    @Override
    @SneakyThrows
    default OUT apply(IN in) {
        return this.applyWithException(in);
    }

}
