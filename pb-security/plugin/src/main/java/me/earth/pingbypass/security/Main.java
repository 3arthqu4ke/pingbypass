package me.earth.pingbypass.security;

import lombok.extern.java.Log;
import me.earth.pingbypass.api.config.properties.PropertyConfigImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

@Log
public class Main {
    public static void main(String[] args) throws SecurityException {
        if (Arrays.stream(args).anyMatch(a -> a.toLowerCase().endsWith("help") || "-h".equalsIgnoreCase(a))) {
            log.info("Please specify the properties %s, %s, %s".formatted(
                    SecurityProperties.KEY_STORE_PASSWORD.getName(),
                    SecurityProperties.KEY_ALIAS.getName(),
                    SecurityProperties.KEY_PASSWORD.getName()));
            return;
        }

        var path = getPingBypassDirectory();
        var propertyConfig = PropertyConfigImpl.tryReadPath(path.resolve("pingbypass.properties"));
        var delegateProperties = new Properties();
        delegateProperties.put(SecurityProperties.ENABLED.getName(), "true");
        var config = new DelegatePropertyConfig(propertyConfig, delegateProperties);

        var alias = config.get(SecurityProperties.KEY_ALIAS);
        var security = path.resolve("security");

        var securityManager = SecurityManagerFactory.fromConfig(security, config);
        log.info("KeyStoreLocation: %s, PublicKey: %s".formatted(
                security.resolve(config.get(SecurityProperties.KEY_STORE_NAME)), alias));

        if (securityManager.hasKeyPair(alias)) {
            log.warning("KeyStore already contains key %s".formatted(alias));
        } else {
            log.info("KeyStore does not contain key %s, adding it...".formatted(alias));
            SecurityManagerImplHelper.addKeyIfNecessary((SecurityManagerImpl) securityManager);
            log.info("Key %s added successfully!".formatted(alias));
        }
    }

    private static Path getPingBypassDirectory() {
        var path = Paths.get("").toAbsolutePath();
        if ("mods".equalsIgnoreCase(path.getFileName().toString())) {
            path = path.getParent();
        }

        return path.resolve("pingbypass");
    }

}
