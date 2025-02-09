package com.store.user.utils;

import com.store.user.config.KeywordsAndConstants;
import com.store.user.error.BadRequestException;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Component
public class EncryptionUtils {

    public static String encrypt(String stringToEncrypt) throws Exception {
        try {
            String password = KeywordsAndConstants.ENCRYPTION_PASSWORD_CHOICE_ONE;
            String salt = KeywordsAndConstants.ENCRYPTION_PASSWORD_SALT_CHOICE;
            SecretKey key = AESUtil.getKeyFromPassword(password, salt);
            IvParameterSpec ivParameterSpec = AESUtil.generateIv();
            return AESUtil.encryptPasswordBased(stringToEncrypt, key, ivParameterSpec);
        } catch (Exception ex) {
            throw new BadRequestException("Failed in Encryption");
        }
    }

    public static String decrypt(String toDecrypt) {
        try {
            String password = KeywordsAndConstants.ENCRYPTION_PASSWORD_CHOICE_ONE;
            String salt = KeywordsAndConstants.ENCRYPTION_PASSWORD_SALT_CHOICE;
            IvParameterSpec ivParameterSpec = AESUtil.generateIv();
            SecretKey key = AESUtil.getKeyFromPassword(password, salt);
            return AESUtil.decryptPasswordBased(toDecrypt, key, ivParameterSpec);
        } catch (Exception ex) {
                throw new BadRequestException();
        }
    }

}


