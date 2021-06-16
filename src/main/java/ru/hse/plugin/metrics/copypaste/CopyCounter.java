package ru.hse.plugin.metrics.copypaste;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.COPY_COUNTER;

public class CopyCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return COPY_COUNTER;
    }

    public CopyCounter() {
        super();
    }

    public CopyCounter(int counter) {
        super(counter);
    }

    @Override
    public void copy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
        inc();
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of copies into clipboard";
    }

    @Override
    @NotNull
    public CopyCounter copy() {
        return new CopyCounter(getCounter());
    }
}
