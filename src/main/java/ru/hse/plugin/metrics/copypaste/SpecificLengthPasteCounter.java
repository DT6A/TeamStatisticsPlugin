package ru.hse.plugin.metrics.copypaste;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;
import ru.hse.plugin.metrics.abstracts.Metric;

import java.util.Objects;

import static ru.hse.plugin.metrics.commons.util.Names.SPECIFIC_LENGTH_PASTE_COUNTER;

public class SpecificLengthPasteCounter extends CountingMetric {
    private final int length;

    @NotNull
    @Override
    protected String getClassName() {
        return SPECIFIC_LENGTH_PASTE_COUNTER;
    }

    public SpecificLengthPasteCounter(int length) {
        super();
        this.length = length;
    }

    public SpecificLengthPasteCounter(int counter, int length) {
        super(counter);
        this.length = length;
    }

    @Override
    public void paste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        if (text.length() == length) {
            inc();
        }
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "(" + length + ")";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of pastes with length " + length;
    }

    @Override
    public String toString() {
        return getClassName() + " " + getCounter() + " " + length;
    }

    @Override
    public boolean isSame(@NotNull Metric metric) {
        if (!super.isSame(metric)) {
            return false;
        }
        SpecificLengthPasteCounter that = (SpecificLengthPasteCounter) metric;
        return this.length == that.length;
    }

    @Override
    public int hashSame() {
        return Objects.hash(super.hashSame(), length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpecificLengthPasteCounter that = (SpecificLengthPasteCounter) o;
        return length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), length);
    }
}
