package ru.hse.plugin.metrics.typed;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.util.Algorithms;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static ru.hse.plugin.metrics.commons.util.Names.SUBSTRING_COUNTER;
import static ru.hse.plugin.util.Constants.BANNED_SYMBOL_FOR_Z_FUNCTION;

public class SubstringCounter extends CountingMetric {

    @NotNull
    private final String substring;
    private final int length;

    public SubstringCounter(@NotNull String substring, int counter) {
        super(counter);
        if (substring.indexOf(BANNED_SYMBOL_FOR_Z_FUNCTION) != -1) {
            throw new RuntimeException("Incorrect word for counting substring");
        }
        this.substring = substring;
        this.length = substring.length();
    }

    public SubstringCounter(@NotNull String substring)  {
        this(substring, 0);
    }

    @Override
    public void updateBeforeCharTyped(char charTyped,
                                      @NotNull Editor editor) {
        // offset -- количество символов, от начала текста до каретки
        inc(updateFromText(
                charTyped,
                editor.getDocument().getImmutableCharSequence(),
                editor.getCaretModel().getOffset()
        ));
    }

    @Override
    protected @NotNull String getClassName() {
        return SUBSTRING_COUNTER;
    }

    @NotNull
    @Override
    public String getName() {
        return getClassName() + "(" + substring + ")";
    }

    @NotNull
    @Override
    public String localStatisticString() {
        return "Number of occurrences of substring \"" + substring + "\"";
    }

    @Override
    public boolean isSame(@NotNull Metric metric) {
        if (!super.isSame(metric)) {
            return false;
        }

        SubstringCounter that = (SubstringCounter) metric;
        return this.substring.equals(that.substring);
    }

    @Override
    public int hashSame() {
        return Objects.hash(substring, length);
    }

    @Override
    public String toString() {
        return getClassName() + " " + substring + " " + getCounter();
    }

    private int updateFromText(char charTyped,
                               CharSequence text,
                               int offset) {
        String previousString = text.subSequence(
                max(0, offset - length + 1),
                min(text.length(), offset + length - 1)
        ).toString();
      //  System.out.println("Char typed: " + charTyped);
     //   System.out.println("Previous String {" + previousString + "}");
        String newString = previousString.substring(0, length - 1)
                + charTyped
                + previousString.substring(length);
       // System.out.println("New String {" + newString + "}");
        int diff =  max(0, Algorithms.zFunction(newString, substring) - Algorithms.zFunction(previousString, substring));
       // System.out.println(diff);
        return diff;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SubstringCounter that = (SubstringCounter) o;
        return length == that.length && substring.equals(that.substring);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), substring, length);
    }

    @Override
    @NotNull
    public SubstringCounter copy() {
        return new SubstringCounter(substring, getCounter());
    }
}
