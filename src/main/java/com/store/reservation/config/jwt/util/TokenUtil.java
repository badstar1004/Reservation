package com.store.reservation.config.jwt.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class TokenUtil {

    /**
     * claims.setId() 암호화
     *
     * @return
     */
    public static String generateRandomToken() {
        UUID uuid = UUID.randomUUID();
        String plainToken = uuid.toString();

        // SHA-256 해시를 사용하여 토큰 암호화
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(plainToken.getBytes());
            String hashedToken = Base64.getEncoder().encodeToString(hashBytes);

            return hashedToken;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
