package ru.hse.plugin.metrics.copypaste;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.COPY_LENGTH_COUNTER;

public class CopyLengthCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return COPY_LENGTH_COUNTER;
    }

    public CopyLengthCounter() {
        super();
    }

    public CopyLengthCounter(int counter) {
        super(counter);
    }

    @Override
    public void copy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
        inc(text.length());
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Length of copies into clipboard";
    }
}
