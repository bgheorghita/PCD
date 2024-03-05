package uaic.fii.pcd.utils;

import java.util.Random;

public class AsciiCharacterGenerator {
    private static final int ASCII_MIN = 32;
    private static final int ASCII_MAX = 126;

    public static byte[] generateAsciiCharacters(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        byte[] asciiCharacters = new byte[length];
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            asciiCharacters[i] = (byte) (random.nextInt(ASCII_MAX - ASCII_MIN + 1) + ASCII_MIN);
        }

        return asciiCharacters;
    }
}
