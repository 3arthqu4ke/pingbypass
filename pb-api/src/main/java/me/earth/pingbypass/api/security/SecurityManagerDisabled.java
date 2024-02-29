package me.earth.pingbypass.api.security;

import me.earth.pingbypass.api.security.SecurityException;
import me.earth.pingbypass.api.security.SecurityManager;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Optional;

public final class SecurityManagerDisabled implements SecurityManager {
    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public PrivateKey getPrivateKey() throws SecurityException {
        throw new SecurityException("Security is disabled");
    }

    @Override
    public Optional<PrivateKey> getPrivateKey(String alias, String password) throws SecurityException {
        throw new SecurityException("Security is disabled");
    }

    @Override
    public Optional<PublicKey> getPublicKey(String alias) throws SecurityException {
        throw new SecurityException("Security is disabled");
    }

    @Override
    public void addKeyPair(String alias, String password, KeyPair keyPair, Certificate... certificates) {

    }

    @Override
    public boolean hasKeyPair(String alias) throws SecurityException {
        throw new SecurityException("Security is disabled");
    }

    @Override
    public void refresh() throws SecurityException {
        throw new SecurityException("Security is disabled");
    }

}
