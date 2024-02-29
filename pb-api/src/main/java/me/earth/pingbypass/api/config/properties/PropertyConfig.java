package me.earth.pingbypass.api.config.properties;

import java.util.function.Supplier;

public interface PropertyConfig {
    <T> T getValue(Property<T> property, Supplier<T> defaultValue);

    default <T> T getNullable(Property<T> property) {
        return getValue(property, () -> null);
    }

    default <T> T get(Property<T> property) {
        T value = getValue(property, () -> null);
        return value == null ? property.getDefaultValue() : value;
    }

    default <T> T get(Property<T> property, T defaultValue) {
        T value = getValue(property, () -> defaultValue);
        return value == null ? property.getDefaultValue() : value;
    }

}
