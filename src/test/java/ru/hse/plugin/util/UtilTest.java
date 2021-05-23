package ru.hse.plugin.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.hse.plugin.util.Util.zipWith;

class UtilTest {
    @Test
    public void testSimpleStreamZipWith() {
        assertEquals(
                List.of(Pair.of(1, 10), Pair.of(2, 20), Pair.of(3, 30)),
                zipWith(Stream.of(1, 2, 3), Stream.of(10, 20, 30), Pair::of).collect(Collectors.toList())
        );
        assertEquals(
                List.of(Pair.of(1, 10), Pair.of(2, 20), Pair.of(3, 30)),
                zipWith(Stream.of(1, 2, 3), Stream.of(10, 20, 30, 40), Pair::of).collect(Collectors.toList())
        );
    }
}
