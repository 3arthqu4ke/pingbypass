package me.earth.pingbypass;

import me.earth.pingbypass.api.security.SecurityException;
import me.earth.pingbypass.api.security.SecurityManager;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Optional;

public class DummySecurityManager implements SecurityManager {
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public PrivateKey getPrivateKey() throws SecurityException {
        return null;
    }

    @Override
    public Optional<PrivateKey> getPrivateKey(String alias, String password) throws SecurityException {
        return Optional.empty();
    }

    @Override
    public Optional<PublicKey> getPublicKey(String alias) {
        return Optional.empty();
    }

    @Override
    public void addKeyPair(String alias, String password, KeyPair keyPair, Certificate... certificates) {

    }

    @Override
    public boolean hasKeyPair(String alias) throws SecurityException {
        return false;
    }

    @Override
    public void refresh() throws SecurityException {

    }
}
