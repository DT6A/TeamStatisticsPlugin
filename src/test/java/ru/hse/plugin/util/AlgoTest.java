package ru.hse.plugin.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.function.ToIntBiFunction;

public class AlgoTest extends AbstractAlgoTest {
    // word -> text -> int
    private ToIntBiFunction<String, String> testingFunction() {
        return (x, y) -> Algorithms.zFunction(y, x);
    }

    private static final int BIG_ITER_NUM = 200;
    private static final int SMALL_ITER_NUM = 20;

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void simpleSubstringCounterTest() {
        testSubstringCount("aba", "abababa");
        testSubstringCount("aa", "aaaaaaaa");
        testSubstringCount("aa", "aa");
        testSubstringCount("a", "");
        testSubstringCount("abca", "abcabc");
        testSubstringCount("abca", "abcabca");
    }

    private static final int RANDOM_SEED = 12345;

    @Test
    public void stressSubstringCounterTest() {
        Random random = new Random(RANDOM_SEED);

        for (int big = 0; big < BIG_ITER_NUM; big++) {
            String basicString = basicString(random);
            for (int small = 0; small < SMALL_ITER_NUM; small++) {
                String smallString = smallString(random);
                testSubstringCount(smallString, basicString);
            }
        }
    }

    private void testSubstringCount(String smallString, String basicString) {
        int expected = naiveSubstringCounter(smallString, basicString);
        int actual = testingFunction().applyAsInt(smallString, basicString);
        Assertions.assertEquals(expected, actual);
    }
}
