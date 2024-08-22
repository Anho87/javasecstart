package se.systementor.javasecstart.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    public static String generateToken(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
