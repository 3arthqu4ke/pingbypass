package me.earth.pingbypass.api.config.properties;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Supplier;

// SOFTTODO: move to security-lib?
@RequiredArgsConstructor
public class PropertyConfigImpl implements PropertyConfig {
    private final Properties properties;

    public PropertyConfigImpl(Path path) throws IOException {
        this(new Properties());
        try (InputStream is = Files.newInputStream(path)) {
            properties.load(is);
        }
    }

    @Override
    public <T> T getValue(Property<T> property, Supplier<T> defaultValue) {
        String toParse = System.getenv(property.getName());
        if (toParse == null) {
            toParse = System.getProperty(property.getName(), properties.getProperty(property.getName()));
            if (toParse == null) {
                return defaultValue.get();
            }
        }

        return property.parse(toParse);
    }

    // TODO: this is dumb
    public static PropertyConfigImpl tryReadPath(Path path) {
        try {
            return new PropertyConfigImpl(path);
        } catch (IOException e) {
            e.printStackTrace();
            return new PropertyConfigImpl(new Properties());
        }
    }

}
