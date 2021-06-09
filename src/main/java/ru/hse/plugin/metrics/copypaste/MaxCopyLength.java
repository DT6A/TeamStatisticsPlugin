package ru.hse.plugin.metrics.copypaste;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.MaxMetric;

import static ru.hse.plugin.metrics.commons.util.Names.MAX_COPY_LENGTH;

public class MaxCopyLength extends MaxMetric {
    @Override
    protected @NotNull String getClassName() {
        return MAX_COPY_LENGTH;
    }

    public MaxCopyLength() {
        super();
    }

    public MaxCopyLength(int maximum) {
        super(maximum);
    }

    @Override
    public void copy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
        updateMax(text.length());
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Maximum paste length";
    }
}
