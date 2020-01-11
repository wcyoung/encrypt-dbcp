package wcyoung.encrypt.dbcp.crypto.encryptor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PBEStringEncryptorTest {

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String encryptKey = "encryptKey";
        String plainText = "password";

        PBEStringEncryptor encryptor = new PBEStringEncryptor(encryptKey);
        String encryptedText = encryptor.encrypt(plainText);
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = encryptor.decrypt(encryptedText);

        assertEquals(plainText, decryptedText);
    }

}
