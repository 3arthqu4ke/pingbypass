package me.earth.pingbypass.api.security;

import lombok.Data;
import me.earth.pingbypass.api.security.SecurityException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Data
public class PrivateKeyManager {
    private final KeyStore keyStore;
    private final Path keyStorePath;
    private final char[] keyStorePassword;
    private final String alias;
    private final char[] keyPassword;

    PrivateKeyManager(Path keyStorePath, String password, String alias, String keyPassword) {
        try {
            this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            this.keyStorePath = keyStorePath;
            this.keyStorePassword = password.toCharArray();
            this.alias = alias;
            this.keyPassword = keyPassword.toCharArray();
            this.load();
        } catch (KeyStoreException e) {
            throw new SecurityException(e);
        }
    }

    public boolean hasKey(String alias) {
        try {
            return keyStore.containsAlias(alias);
        } catch (KeyStoreException e) {
            throw new SecurityException(e);
        }
    }

    public PrivateKey getPrimaryPrivateKey() {
        return getPrivateKey(alias, keyPassword);
    }

    public PrivateKey getPrivateKey(String alias, char[] password) {
        try {
            Key key = keyStore.getKey(alias, password);
            if (key instanceof PrivateKey privateKey) {
                return privateKey;
            }

            throw new SecurityException(alias + " is not a PrivateKey: " + key);
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SecurityException("Failed to load PrivateKey" + alias, e);
        }
    }

    public void addKey(String alias, String password, PrivateKey key, Certificate... certificates) {
        try {
            keyStore.setKeyEntry(alias, key, password.toCharArray(), certificates);
            this.save();
        } catch (KeyStoreException e) {
            throw new SecurityException("Failed to set KeyEntry " + alias, e);
        }
    }

    public void save() {
        try (var outputStream = Files.newOutputStream(keyStorePath)) {
            keyStore.store(outputStream, keyStorePassword);
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new SecurityException("Failed to save KeyStore " + keyStorePath, e);
        }
    }

    public void load() {
        try {
            if (Files.exists(keyStorePath)) {
                try (var inputStream = Files.newInputStream(keyStorePath)) {
                    keyStore.load(inputStream, keyStorePassword);
                }
            } else {
                keyStore.load(null);
            }
        } catch (CertificateException | IOException | NoSuchAlgorithmException e) {
            throw new SecurityException("Failed to load KeyStore " + keyStorePath, e);
        }
    }

}
