package me.earth.pingbypass.api.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Optional;

public interface SecurityManager {
    /**
     * @return {@code true} if this SecurityManager can be used. Otherwise, do not call methods, they could cause an
     * UnsupportedOperationException.
     */
    boolean isEnabled();

    PrivateKey getPrivateKey() throws SecurityException;

    Optional<PrivateKey> getPrivateKey(String alias, String password) throws SecurityException;

    Optional<PublicKey> getPublicKey(String alias);

    void addKeyPair(String alias, String password, KeyPair keyPair, Certificate... certificates);

    boolean hasKeyPair(String alias) throws SecurityException;

    void refresh() throws SecurityException;

}
