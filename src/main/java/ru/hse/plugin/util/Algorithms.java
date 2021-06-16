package ru.hse.plugin.util;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static ru.hse.plugin.util.Constants.BANNED_SYMBOL_FOR_Z_FUNCTION;

public class Algorithms {

    private Algorithms() {}


    public static int zFunction(String text, String word) {
        int[] z = initZFunction(word + BANNED_SYMBOL_FOR_Z_FUNCTION + text);
        int numberOfOccurrence = 0;
        for (int i = 0; i < text.length(); i++) {
            if (z[word.length() + 1 + i] == word.length()) {
                numberOfOccurrence++;
            }
        }
        return numberOfOccurrence;
    }

    private static int[] initZFunction(String text) {
        int n = text.length();
        int[] z = new int[n];
        z[0] = 0;
        int l = 0;
        int r = 0;
        for (int i = 1; i < n; i++) {
            int k = max(0, min(r - i, z[i - l]));
            while ((i + k < n) && text.charAt(i + k) == text.charAt(k)) {
                k++;
            }
            z[i] = k;
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        return z;
    }
}
