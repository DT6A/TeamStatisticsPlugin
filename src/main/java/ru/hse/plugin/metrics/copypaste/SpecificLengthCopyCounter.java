package ru.hse.plugin.metrics.copypaste;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;
import ru.hse.plugin.metrics.abstracts.Metric;

import java.util.Objects;

import static ru.hse.plugin.metrics.commons.util.Names.SPECIFIC_LENGTH_COPY_COUNTER;

public class SpecificLengthCopyCounter  extends CountingMetric {
    private final int length;

    @NotNull
    @Override
    protected String getClassName() {
        return SPECIFIC_LENGTH_COPY_COUNTER;
    }

    public SpecificLengthCopyCounter(int length) {
        super();
        this.length = length;
    }

    public SpecificLengthCopyCounter(int counter, int length) {
        super(counter);
        this.length = length;
    }

    @Override
    public void copy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
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
        return "Number of copies with length " + length;
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
        SpecificLengthCopyCounter that = (SpecificLengthCopyCounter) metric;
        return this.length == that.length;
    }

    @Override
    public int hashSame() {
        return Objects.hash(super.hashSame(), length);
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
        SpecificLengthCopyCounter that = (SpecificLengthCopyCounter) o;
        return length == that.length;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), length);
    }

    @Override
    @NotNull
    public SpecificLengthCopyCounter copy() {
        return new SpecificLengthCopyCounter(getCounter(), length);
    }
}
