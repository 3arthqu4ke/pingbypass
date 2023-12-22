package me.earth.pingbypass.security;

import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.properties.Property;
import me.earth.pingbypass.api.config.properties.PropertyConfig;

import java.util.Properties;
import java.util.function.Supplier;

@RequiredArgsConstructor
final class DelegatePropertyConfig implements PropertyConfig {
    private final PropertyConfig delegate;
    private final Properties properties;

    @Override
    public <T> T getValue(Property<T> property, Supplier<T> defaultValue) {
        String value = String.valueOf(properties.get(property.getName()));
        return value == null ? delegate.getValue(property, defaultValue) : property.parse(value);
    }

}
