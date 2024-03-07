package me.earth.pingbypass.api.security;

import me.earth.pingbypass.api.config.properties.Property;
import me.earth.pingbypass.api.config.properties.PropertyConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class SecurityManagerFactory {
    public static SecurityManager fromConfig(Path path, PropertyConfig config) {
        return fromConfig(path, config, new KeyPairServiceRSA());
    }

    public static SecurityManager fromConfig(Path path, PropertyConfig config, KeyPairService keyPairService) {
        if (!require(config, SecurityProperties.ENABLED)) {
            return new SecurityManagerDisabled();
        }

        var keyStore = path.resolve(require(config, SecurityProperties.KEY_STORE_NAME));
        var keyAlias = require(config, SecurityProperties.KEY_ALIAS);
        var keyStorePassword = require(config, SecurityProperties.KEY_STORE_PASSWORD);
        var keyPassword = require(config, SecurityProperties.KEY_PASSWORD);

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new SecurityException("Could not create " + path, e);
        }

        var privateKeyManager = new PrivateKeyManager(keyStore, keyStorePassword, keyAlias, keyPassword);
        privateKeyManager.load();

        var publicKeyManager = new PublicKeyManager(keyPairService, path);
        publicKeyManager.load();

        return new SecurityManagerImpl(privateKeyManager, publicKeyManager, keyPairService);
    }

    private static <T> T require(PropertyConfig config, Property<T> property) {
        return Objects.requireNonNull(config.get(property), "Please set '%s'!'".formatted(property.getName()));
    }

}
