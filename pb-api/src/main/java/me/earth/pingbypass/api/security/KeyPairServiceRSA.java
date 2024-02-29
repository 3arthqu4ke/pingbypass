package me.earth.pingbypass.api.security;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


final class KeyPairServiceRSA implements KeyPairService {
    private static final Base64.Encoder MIME_ENCODER = Base64.getMimeEncoder(76, "\n".getBytes(StandardCharsets.UTF_8));
    private static final String ASYMMETRIC_ALGORITHM = "RSA";
    private static final String PEM_RSA_PRIVATE_KEY_HEADER = "-----BEGIN RSA PRIVATE KEY-----";
    private static final String PEM_RSA_PRIVATE_KEY_FOOTER = "-----END RSA PRIVATE KEY-----";
    private static final String RSA_PUBLIC_KEY_HEADER = "-----BEGIN RSA PUBLIC KEY-----";
    private static final String RSA_PUBLIC_KEY_FOOTER = "-----END RSA PUBLIC KEY-----";

    @Override
    public String toString(PublicKey publicKey) {
        if (!ASYMMETRIC_ALGORITHM.equals(publicKey.getAlgorithm())) {
            throw new IllegalArgumentException("Public key must be RSA");
        }

        return "%s\n%s\n%s\n".formatted(
                RSA_PUBLIC_KEY_HEADER,
                MIME_ENCODER.encodeToString(publicKey.getEncoded()),
                RSA_PUBLIC_KEY_FOOTER);
    }

    @Override
    public String toString(PrivateKey privateKey) {
        if (!ASYMMETRIC_ALGORITHM.equals(privateKey.getAlgorithm())) {
            throw new IllegalArgumentException("Private key must be RSA");
        }

        return "%s\n%s\n%s\n".formatted(
                PEM_RSA_PRIVATE_KEY_HEADER,
                MIME_ENCODER.encodeToString(privateKey.getEncoded()),
                PEM_RSA_PRIVATE_KEY_HEADER);
    }

    @Override
    public PrivateKey parsePrivateKey(String string) throws SecurityException {
        return toKey(string, PEM_RSA_PRIVATE_KEY_HEADER, PEM_RSA_PRIVATE_KEY_FOOTER, this::byteToPrivateKey);
    }

    @Override
    public PublicKey parsePublicKey(String string) throws SecurityException {
        return toKey(string, RSA_PUBLIC_KEY_HEADER, RSA_PUBLIC_KEY_FOOTER, this::byteToPublicKey);
    }

    private <T extends Key> T toKey(String key, String header, String footer, ByteArrayToKeyFunction<T> toKey)
            throws SecurityException {
        var headerIndex = key.indexOf(header);
        if (headerIndex != -1) {
            var footerIndex = key.indexOf(footer, headerIndex += header.length());
            key = key.substring(headerIndex, footerIndex + 1);
        }

        try {
            return toKey.apply(Base64.getMimeDecoder().decode(key));
        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    private PrivateKey byteToPrivateKey(byte[] bytes) throws SecurityException {
        try {
            var encodedKeySpec = new PKCS8EncodedKeySpec(bytes);
            var keyFactory = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM);
            return keyFactory.generatePrivate(encodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException(e);
        }
    }

    private PublicKey byteToPublicKey(byte[] bytes) throws SecurityException {
        try {
            var encodedKeySpec = new X509EncodedKeySpec(bytes);
            var keyFactory = KeyFactory.getInstance(ASYMMETRIC_ALGORITHM);
            return keyFactory.generatePublic(encodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException(e);
        }
    }

    @FunctionalInterface
    private interface ByteArrayToKeyFunction<T extends Key> {
        T apply(byte[] bytes) throws SecurityException;
    }

}
