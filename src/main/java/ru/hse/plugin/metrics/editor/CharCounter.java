package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;
import ru.hse.plugin.metrics.abstracts.Metric;

import java.util.Objects;

import static ru.hse.plugin.metrics.commons.Names.CHAR_COUNTER;

public class CharCounter extends CountingMetric {
    private final char character;

    public CharCounter(char character, int counter) {
        super(counter);
        this.character = character;
    }

    public CharCounter(char character) {
        this(character, 0);
    }

    @Override
    public void updateCharTyped(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (charTyped == character) {
            inc();
        }
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "(" + Character.getNumericValue(character) + ")";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of occurrences of '" + character + "'";
    }

    @NotNull
    @Override
    protected String getClassName() {
        return CHAR_COUNTER;
    }

    @Override
    public boolean isSame(@NotNull Metric metric) {
        if (!super.isSame(metric)) {
            return false;
        }
        CharCounter that = (CharCounter) metric;
        return this.character == that.character;
    }

    @Override
    public int hashSame() {
        return Objects.hash(super.hashSame(), character);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharCounter that = (CharCounter) o;
        return character == that.character;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }
}
