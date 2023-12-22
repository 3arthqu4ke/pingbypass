package me.earth.pingbypass.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityManagerImplHelper {
    private static final KeyPairFactory keyPairFactory = RsaX509KeypairFactory.INSTANCE;

    public static void addKeyIfNecessary(SecurityManagerImpl securityManager) throws SecurityException {
        // TODO: instead of requiring to expose this we could also somehow get the config with alias and password
        //  that way we could hide SecurityManagerImpl and its internals
        var privateKeyManager = securityManager.getPrivateKeyManager();
        if (!securityManager.hasKeyPair(privateKeyManager.getAlias())) {
            KeyPairWithCertificate keyPairWithCertificate = keyPairFactory.create();
            securityManager.addKeyPair(privateKeyManager.getAlias(),
                                       new String(privateKeyManager.getKeyPassword()),
                                       keyPairWithCertificate.keyPair(),
                                       keyPairWithCertificate.certificates());
        }
    }

}
