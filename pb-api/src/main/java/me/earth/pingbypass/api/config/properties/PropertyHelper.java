package me.earth.pingbypass.api.config.properties;

import lombok.Data;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

// SOFTTODO: move to security-lib?
@UtilityClass
public class PropertyHelper {
    public static Property<String> string(String name, String def) {
        return new PropertyImpl<>(name, def, Function.identity());
    }

    public static Property<String[]> array(String name, String delimiter, String... def) {
        return new PropertyImpl<>(name, def, nullable(value -> {
            if (value.isEmpty()) {
                return null;
            }

            return value.split(delimiter);
        }));
    }

    public static Property<Integer> number(String name, int def) {
        return new PropertyImpl<>(name, def, nullable(value -> {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
                throw new NumberFormatException(String.format(
                    "Couldn't parse Property %s, %s is not a number!",
                    name, value));
            }
        }));
    }

    public static Property<Boolean> bool(String name, boolean def) {
        return new PropertyImpl<>(name, def, nullable(Boolean::parseBoolean));
    }

    public static <T extends Enum<T>> Property<T> constant(String name,
                                                           Class<T> type,
                                                           T def) {
        return new PropertyImpl<>(name, def, nullable(value -> {
            for (T t : type.getEnumConstants()) {
                if (t.name().equalsIgnoreCase(value)) {
                    return t;
                }
            }

            return null;
        }));
    }

    private static <T> Function<String, T> nullable(Function<String, T> func) {
        return value -> {
            if (value == null) {
                return null;
            }

            return func.apply(value);
        };
    }

    @Data
    private final class PropertyImpl<T> implements Property<T> {
        private final String name;
        private final T defaultValue;
        private final Function<String, T> function;

        @Override
        public T parse(String value) {
            return function.apply(value);
        }
    }

}