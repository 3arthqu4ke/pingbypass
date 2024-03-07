package me.earth.pingbypass.api.security;

import me.earth.pingbypass.api.security.SecurityException;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairService {
    String toString(PublicKey publicKey);

    String toString(PrivateKey privateKey);

    PrivateKey parsePrivateKey(String string) throws SecurityException;

    PublicKey parsePublicKey(String string) throws SecurityException;

}
