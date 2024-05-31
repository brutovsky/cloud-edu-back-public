package com.nakytniak.backend.auth0.utils;

import java.security.SecureRandom;

public final class PasswordGenerator {

    private PasswordGenerator() {

    }

    private static final int RANDOM_CHARS_LENGTH = 4;
    private static final int MINIMAL_LENGTH = 8;
    private static final int DEFAULT_LENGTH = 12;
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+<>,.?";

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARS;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomPassword() {
        return generateRandomPassword(DEFAULT_LENGTH);
    }

    public static String generateRandomPassword(final int length) {
        if (length < MINIMAL_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Password length should be at least %s characters", MINIMAL_LENGTH));
        }

        StringBuilder password = new StringBuilder(length);

        // Ensure password contains at least one character from each category
        password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(RANDOM.nextInt(SPECIAL_CHARS.length())));

        // Fill the remaining characters randomly
        for (int i = RANDOM_CHARS_LENGTH; i < length; i++) {
            password.append(ALL_CHARS.charAt(RANDOM.nextInt(ALL_CHARS.length())));
        }

        // Shuffle the characters to avoid predictable sequences
        return shuffleString(password.toString());
    }

    private static String shuffleString(final String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = RANDOM.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
}
