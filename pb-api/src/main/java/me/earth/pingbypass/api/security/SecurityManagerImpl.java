package me.earth.pingbypass.api.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Optional;

@Data
@RequiredArgsConstructor
// TODO: currently this has to be public for the SecurityManagerFactory mixin, see SecurityManagerImplHelper
public final class SecurityManagerImpl implements SecurityManager {
    private final PrivateKeyManager privateKeyManager;
    private final PublicKeyManager publicKeyManager;
    private final KeyPairService keyPairService;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public PrivateKey getPrivateKey() {
        // TODO: cache this?
        return privateKeyManager.getPrimaryPrivateKey();
    }

    @Override
    public Optional<PrivateKey> getPrivateKey(String alias, String password) {
        return Optional.ofNullable(privateKeyManager.getPrivateKey(alias, password.toCharArray()));
    }

    @Override
    public Optional<PublicKey> getPublicKey(String alias) {
        return Optional.ofNullable(publicKeyManager.getPublicKeys().get(alias));
    }

    @Override
    public void addKeyPair(String alias, String password, KeyPair keyPair, Certificate... certificates) {
        privateKeyManager.addKey(alias, password, keyPair.getPrivate(), certificates);
        publicKeyManager.addPublicKey(alias, keyPair.getPublic());
    }

    @Override
    public boolean hasKeyPair(String alias) throws SecurityException {
        return privateKeyManager.hasKey(alias)
                && publicKeyManager.getPublicKeys().get(alias) != null;
    }

    @Override
    public void refresh() {
        privateKeyManager.load();
        publicKeyManager.load();
    }

}
