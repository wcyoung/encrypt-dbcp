package wcyoung.encrypt.dbcp.crypto.encryptor;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import wcyoung.encrypt.dbcp.crypto.CryptoConst;

public class PBEStringEncryptor {

    private SecretKey secretKey;
    private String algorithm;

    public PBEStringEncryptor(String encryptKey) throws Exception {
        this(encryptKey, CryptoConst.ALGORITHM);
    }

    public PBEStringEncryptor(String encryptKey, String algorithm) throws Exception {
        this.algorithm = algorithm;
        this.secretKey = generateSecretKey(encryptKey);
    }

    public String encrypt(String plainText) throws Exception {
        byte[] salt = new byte[8];
        Random random = new Random();
        random.nextBytes(salt);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, CryptoConst.ITERATIONS);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes());

        Encoder encoder = Base64.getEncoder();
        String saltString = encoder.encodeToString(salt);
        String encryptedString = encoder.encodeToString(encrypted);

        return saltString + encryptedString;
    }

    public String decrypt(String encryptedText) throws Exception {
        String saltString = encryptedText.substring(0, 12);
        String encryptedString = encryptedText.substring(12);

        Decoder decoder = Base64.getDecoder();
        byte[] salt = decoder.decode(saltString);
        byte[] encrypted = decoder.decode(encryptedString);

        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, CryptoConst.ITERATIONS);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        byte[] plainTextBytes = cipher.doFinal(encrypted);

        return new String(plainTextBytes);
    }

    private SecretKey generateSecretKey(String encryptKey) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(encryptKey.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(keySpec);
        return key;
    }

}
