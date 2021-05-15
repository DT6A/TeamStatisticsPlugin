package ru.hse.plugin;

import jnr.ffi.annotations.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.plugin.metrics.WordCounter;

import java.io.SequenceInputStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


class WordCounterTest {

    @Test
    public void testUpdateFromText() {
        WordCounter wordCounter = new WordCounter("Hello");
        Method method;
        try {
             method = wordCounter.getClass().getDeclaredMethod(
                     "updateFromText",
                     char.class,
                     CharSequence.class,
                     int.class
             );
            method.setAccessible(true);
            Assertions.assertTrue((boolean) method.invoke(wordCounter, 'H', "Hello", 1));
            Assertions.assertFalse((boolean) method.invoke(wordCounter, 'B', "Bello Hello", 1));
            Assertions.assertFalse((boolean) method.invoke(wordCounter, 'o', "Bello Hello", 5));
            Assertions.assertFalse((boolean) method.invoke(wordCounter, 'o', "HelloHello", 5));
            Assertions.assertFalse((boolean) method.invoke(wordCounter, 'B', "Hello Bello", 7));
            Assertions.assertFalse((boolean) method.invoke(wordCounter, 'o', "DHello Bello", 6));
            Assertions.assertTrue((boolean) method.invoke(wordCounter, ' ', "Hello Bello", 6));
            Assertions.assertTrue((boolean) method.invoke(wordCounter, ' ', "Bello Hello", 6));
        } catch (Exception e) {
            fail();
        }
    }

}