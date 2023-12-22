package me.earth.pingbypass.security;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

enum RsaX509KeypairFactory implements KeyPairFactory {
    INSTANCE;

    private static final String SIG_ALG = "SHA1withRSA";
    private static final int KEY_SIZE = 2048;
    private static final String ALG = "RSA";

    @Override
    public KeyPairWithCertificate create() throws SecurityException {
        try {
            return createInternal();
        } catch (NoSuchAlgorithmException
                 | NoSuchProviderException
                 | CertificateException
                 | IOException
                 | OperatorCreationException e) {
            throw new SecurityException(e);
        }
    }

    private KeyPairWithCertificate createInternal() throws NoSuchAlgorithmException, NoSuchProviderException,
                                                        CertificateException, IOException, OperatorCreationException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        X509Certificate certificate = getCertificate(keyPair);
        return new KeyPairWithCertificate(keyPair, certificate);
    }

    private X509Certificate getCertificate(KeyPair keyPair)
            throws IOException, OperatorCreationException, CertificateException {
        var publicKey = (RSAPublicKey) keyPair.getPublic();
        var privateKey = keyPair.getPrivate();

        var from = Date.from(Instant.now().minus(Duration.ofDays(1)));
        var until = Date.from(Instant.now().plus(Duration.ofDays(365 * 10)));

        var issuer = new X500Name(new X500Principal("cn=PingBypass").getName());
        var publicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(
                new RSAKeyParameters(false, publicKey.getModulus(), publicKey.getPublicExponent()));

        var builder = new X509v1CertificateBuilder(issuer, BigInteger.ONE, from, until, issuer, publicKeyInfo);
        var signer = new JcaContentSignerBuilder(SIG_ALG).setProvider(new BouncyCastleProvider()).build(privateKey);
        var holder = builder.build(signer);
        var converter = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider());

        return converter.getCertificate(holder);
    }

}
