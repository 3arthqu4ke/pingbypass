package me.earth.pingbypass.api.config.properties;

public interface Property<T> {
    T parse(String value);

    T getDefaultValue();

    String getName();

}
