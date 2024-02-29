package me.earth.pingbypass.api.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtil {
    @SafeVarargs
    public static <T> Stream<T> add(Stream<T> stream, T... args) {
        return Stream.concat(stream, Arrays.stream(args));
    }

}
