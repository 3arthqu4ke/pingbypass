package me.earth.pingbypass.api.security;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.api.security.SecurityException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@UtilityClass
public class CipherUtil {
    public byte[] toByteArray(int value) {
        return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
    }

    public boolean isChallengeValid(byte[] challenge, byte[] encrypted, Key key) {
        try {
            return Arrays.equals(challenge, decryptUsingKey(key, encrypted));
        } catch (SecurityException e) {
            return false;
        }
    }

    public byte[] encryptUsingKey(Key key, byte[] bs) throws SecurityException {
        return cipherData(Cipher.ENCRYPT_MODE, key, bs);
    }

    public byte[] decryptUsingKey(Key key, byte[] bs) throws SecurityException {
        return cipherData(Cipher.DECRYPT_MODE, key, bs);
    }

    private byte[] cipherData(int opMode, Key key, byte[] bs) throws SecurityException {
        try {
            return setupCipher(opMode, key.getAlgorithm(), key).doFinal(bs);
        } catch (IllegalBlockSizeException
                | NoSuchPaddingException
                | BadPaddingException
                | NoSuchAlgorithmException
                | InvalidKeyException e) {
            throw new SecurityException(e);
        }
    }

    private Cipher setupCipher(int opMode, String string, Key key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(string);
        cipher.init(opMode, key);
        return cipher;
    }

}
