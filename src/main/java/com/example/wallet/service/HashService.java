package com.example.wallet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class HashService {

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public String SHA512Encrypt(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String encodedPassword = no.toString(16);

            while (encodedPassword.length() < 32) {
                encodedPassword = "0" + encodedPassword;
            }

            return encodedPassword;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String HMACEncrypt(String password) {
        final String HMAC_SHA512 = "HmacSHA512";
        Mac sha512Hmac;
        String result="";
        try {
            sha512Hmac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(keyValue, HMAC_SHA512);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(password.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace(); }
        finally {
        }

        return result;
    }

    public String AESEncrypt(String password) {
        byte[] encVal = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            encVal = c.doFinal(password.getBytes());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(encVal);
    }

    public String AESDecrypt(String encodedPassword) {
        byte[] decValue = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(encodedPassword);
            decValue = c.doFinal(decodedValue);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new String(decValue);
    }

    private static Key generateKey() {
        return new SecretKeySpec(keyValue, ALGO);
    }

    public byte[] calculateMD5Hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            return messageDigest;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
