package me.earth.pingbypass.security;

import java.security.KeyPair;
import java.security.cert.Certificate;

public record KeyPairWithCertificate(KeyPair keyPair, Certificate... certificates) {

}
