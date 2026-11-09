package com.qveto.exam.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public final class PasswordUtil {
    private static final Argon2 ARGON2 = Argon2Factory.create(Argon2Types.ARGON2id);
    private static final int ITERATIONS = 3;
    private static final int MEMORY_KIB = 65536; // 64 MiB
    private static final int PARALLELISM = 2;

    private PasswordUtil() {}

    public static String hashPassword(char[] rawPassword) {
        if (isBlank(rawPassword)) {
            throw new IllegalArgumentException("Password cannot be blank.");
        }
        try {
            return ARGON2.hash(ITERATIONS, MEMORY_KIB, PARALLELISM, rawPassword);
        } finally {
            ARGON2.wipeArray(rawPassword);
        }
    }

    public static boolean verifyPassword(char[] rawPassword, String hashedPassword) {
        if (isBlank(rawPassword) || isBlank(hashedPassword)) {
            if (rawPassword != null) {
                ARGON2.wipeArray(rawPassword);
            }
            return false;
        }
        try {
            return ARGON2.verify(hashedPassword, rawPassword);
        } finally {
            ARGON2.wipeArray(rawPassword);
        }
    }

    public static boolean isBlank(char[] array) {
        if (array == null || array.length == 0) return true;
        for (char c : array) {
            if (!Character.isWhitespace(c)) return false;
        }
        return true;
    }

    public static boolean isBlank(String string) {
        return string == null || string.isBlank();
    }
}
