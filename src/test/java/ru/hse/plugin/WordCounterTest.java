package ru.hse.plugin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hse.plugin.metrics.editor.WordCounter;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("SpellCheckingInspection")
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
            Assertions.assertEquals(1, method.invoke(wordCounter, 'H', "Hello", 1));
            Assertions.assertEquals(0, method.invoke(wordCounter, 'B', "Bello Hello", 1));
            Assertions.assertEquals(0, method.invoke(wordCounter, 'o', "Bello Hello", 5));
            Assertions.assertEquals(0, method.invoke(wordCounter, 'o', "HelloHello", 5));
            Assertions.assertEquals(0, method.invoke(wordCounter, 'B', "Hello Bello", 7));
            Assertions.assertEquals(0, method.invoke(wordCounter, 'o', "DHello Bello", 6));
            Assertions.assertEquals(1, method.invoke(wordCounter, ' ', "Hello Bello", 6));
            Assertions.assertEquals(1, method.invoke(wordCounter, ' ', "Bello Hello", 6));
            Assertions.assertEquals(2, method.invoke(wordCounter, ' ', "Hello Hello", 6));
        } catch (Exception e) {
            fail();
        }
    }

}