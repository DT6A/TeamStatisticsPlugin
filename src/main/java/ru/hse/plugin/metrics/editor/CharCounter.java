package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;

import java.util.Objects;

import static ru.hse.plugin.metrics.commons.Names.CHAR_COUNTER;

public class CharCounter extends Metric {
    private final char character;
    private int numberOfOccurrences;
    // TODO test
    public CharCounter(char character, int numberOfOccurrences) {
        this.character = character;
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public CharCounter(char character) {
        this(character, 0);
    }

    @Override
    public void updateCharTyped(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (charTyped == character) {
            numberOfOccurrences++;
        }
    }

    @Override
    public void clear() {
        numberOfOccurrences = 0;
    }

    @Override
    public String getInfo() {
        return String.valueOf(numberOfOccurrences);
    }

    @Override
    public @NotNull String getName() {
        return CHAR_COUNTER + "(" + Character.getNumericValue(character) + ")";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        CharCounter that = cast(metric, getClass());

        if (this.character != that.character) {
            throw new RuntimeException("Metrics are expected to be same");
        }

        this.numberOfOccurrences += that.numberOfOccurrences;

        that.clear();
    }

    @Override
    @NotNull
    public MetricJComponentWrapper makeComponent(Metric additional) {
        CharCounter that = cast(additional, CharCounter.class);
        return new CounterJComponentWrapper() {
            @Override
            protected int count() {
                int counter = CharCounter.this.numberOfOccurrences;

                counter += that.numberOfOccurrences;

                return counter;
            }
        };
    }

    @NotNull
    @Override
    public String localStatisticString() {
        return "Number of occurrences of '" + character + "'";
    }

    @Override
    public String toString() {
        return CHAR_COUNTER + " " + Character.getNumericValue(character) + " " + numberOfOccurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharCounter that = (CharCounter) o;
        return character == that.character;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }
}
