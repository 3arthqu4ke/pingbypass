package me.earth.pingbypass.security;

public interface KeyPairFactory {
    KeyPairWithCertificate create() throws SecurityException;

}
