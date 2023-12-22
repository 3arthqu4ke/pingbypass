package me.earth.pingbypass.security;

import lombok.SneakyThrows;
import me.earth.pingbypass.api.config.properties.Property;
import me.earth.pingbypass.api.config.properties.PropertyConfig;
import me.earth.pingbypass.api.security.SecurityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityManagerTest {
    private static final String KEY_STORE_PASSWORD = "testKeyStorePassword";
    private static final String KEY_ALIAS = "testAlias";
    private static final String KEY_PASSWORD = "testPass";
    private static final Path BASE = Paths.get("test");
    private static final Path PATH = Paths.get("test", "security");

    @BeforeAll
    @AfterAll
    @SneakyThrows
    public static void deleteDirectory() {
        Files.createDirectories(BASE);
        try (var files = Files.walk(BASE)) {
            //noinspection ResultOfMethodCallIgnored
            files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }

        assertFalse(Files.exists(BASE));
    }

    @Test
    @SneakyThrows
    public void testSecurityManager() {
        var securityManager = setupSecurityManager();
        assertInstanceOf(SecurityManagerImpl.class, securityManager);
        SecurityManagerImplHelper.addKeyIfNecessary((SecurityManagerImpl) securityManager);

        assertTrue(Files.exists(PATH.resolve(".keystore")));
        assertTrue(Files.exists(PATH.resolve(KEY_ALIAS + ".pem")));
        assertTrue(securityManager.hasKeyPair(KEY_ALIAS));

        var privateKey = securityManager.getPrivateKey();
        var privateKey1 = securityManager.getPrivateKey(KEY_ALIAS, KEY_PASSWORD);
        assertTrue(privateKey1.isPresent());
        assertEquals(privateKey, privateKey1.get());

        var publicKey = securityManager.getPublicKey(KEY_ALIAS);
        assertTrue(publicKey.isPresent());

        var challenge = CipherUtil.toByteArray(new Random().nextInt());
        var encrypted_public = CipherUtil.encryptUsingKey(publicKey.get(), challenge);
        var encrypted_private = CipherUtil.encryptUsingKey(privateKey, challenge);

        assertTrue(CipherUtil.isChallengeValid(challenge, encrypted_public, privateKey));
        assertFalse(CipherUtil.isChallengeValid(challenge, encrypted_public, publicKey.get()));
        assertTrue(CipherUtil.isChallengeValid(challenge, encrypted_private, publicKey.get()));
        assertFalse(CipherUtil.isChallengeValid(challenge, encrypted_private, privateKey));

        securityManager = setupSecurityManager();
        assertTrue(securityManager.hasKeyPair(KEY_ALIAS));
    }

    @SneakyThrows
    private SecurityManager setupSecurityManager() {
        var properties = new Properties();
        properties.put(SecurityProperties.ENABLED.getName(), true);
        properties.put(SecurityProperties.KEY_ALIAS.getName(), KEY_ALIAS);
        properties.put(SecurityProperties.KEY_STORE_PASSWORD.getName(), KEY_STORE_PASSWORD);
        properties.put(SecurityProperties.KEY_PASSWORD.getName(), KEY_PASSWORD);
        return SecurityManagerFactory.fromConfig(PATH, new PropertyConfig() {
            @Override
            public <T> T getValue(Property<T> property, Supplier<T> defaultValue) {
                var string = properties.get(property.getName());
                if (string == null) {
                    return defaultValue.get();
                }

                return property.parse(String.valueOf(string));
            }
        });
    }

}
