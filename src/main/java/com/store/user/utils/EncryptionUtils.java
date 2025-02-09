package com.store.user.utils;

import com.store.user.config.KeywordsAndConstants;
import com.store.user.error.BadRequestException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class EncryptionUtils {

    public static String encrypt(String data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(
                getAESKeyFromSecretKey(KeywordsAndConstants.SECRET_KEY_FOR_PASSWORD_ENCRYPTION),
                KeywordsAndConstants.ALGORITHM
        );
        Cipher cipher = Cipher.getInstance(KeywordsAndConstants.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(
                getAESKeyFromSecretKey(KeywordsAndConstants.SECRET_KEY_FOR_PASSWORD_ENCRYPTION),
                KeywordsAndConstants.ALGORITHM
        );
        Cipher cipher = Cipher.getInstance(KeywordsAndConstants.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }

    private static byte[] getAESKeyFromSecretKey(String secretKey) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(KeywordsAndConstants.MESSAGE_DIGEST_ALGORITHM);
        return digest.digest(secretKey.getBytes(KeywordsAndConstants.UTF_8));
    }

    public String decryptOffset(String toDecrypt) {
        try {
            String password = KeywordsAndConstants.ENCRYPTION_PASSWORD_CHOICE_ONE; // Using only one type
            String salt = KeywordsAndConstants.ENCRYPTION_PASSWORD_SALT_CHOICE;

            IvParameterSpec ivParameterSpec = AESUtil.generateIv();
            SecretKey key = AESUtil.getKeyFromPassword(password, salt);

            return AESUtil.decryptPasswordBased(toDecrypt, key, ivParameterSpec);
        } catch (Exception ex) {
            throw new BadRequestException("Decryption failed >> "+ex);
        }
    }

}


