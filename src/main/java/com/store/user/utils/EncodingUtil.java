package com.store.user.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class EncodingUtil {

    public static String encode(String toEncode) {
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    public static String decode(String toDecode) {
        return new String(Base64.getDecoder().decode(toDecode));
    }
}