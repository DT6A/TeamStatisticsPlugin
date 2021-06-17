package ru.hse.plugin.util;

import java.util.Random;

public abstract class AbstractAlgoTest {
    private final static char LOWER_BOUND = 'a';
    private final static char UPPER_BOUND = 'd';
    private final static int BASIC_STRING_SIZE = 5000;
    private final static int SMALL_STRING_SIZE = 5;

    private char randomChar(Random rnd) {
        return (char) (rnd.nextInt(UPPER_BOUND - LOWER_BOUND + 1) + (int) LOWER_BOUND);
    }

    private String smallRangeRandomString(Random rnd, int size) {
        char[] stringArr = new char[size];
        for (int i = 0; i < size; i++) {
            stringArr[i] = randomChar(rnd);
        }
        return new String(stringArr);
    }

    protected String smallString(Random rnd) {
        return smallRangeRandomString(rnd, SMALL_STRING_SIZE);
    }

    protected String basicString(Random rnd) {
        return smallRangeRandomString(rnd, BASIC_STRING_SIZE);
    }

    protected int naiveSubstringCounter(String string, String text) {
        int counter = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.substring(i).startsWith(string)) {
                counter++;
            }
        }

        return counter;
    }
}
