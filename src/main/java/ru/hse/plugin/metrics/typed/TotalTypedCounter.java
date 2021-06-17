package ru.hse.plugin.metrics.typed;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.TOTAL_TYPED_COUNTER;

public class TotalTypedCounter extends CountingMetric {

    public TotalTypedCounter(int counter) {
        super(counter);
    }

    public TotalTypedCounter() {
        super(0);
    }

    public void updateBeforeCharTyped(
            char charTyped,
            @NotNull Project project,
            @NotNull Editor editor,
            @NotNull PsiFile file,
            @NotNull FileType fileType
    ) {
        inc();
    }

    @Override
    protected @NotNull String getClassName() {
        return TOTAL_TYPED_COUNTER;
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of total typed symbols";
    }

    @Override
    @NotNull
    public TotalTypedCounter copy() {
        return new TotalTypedCounter(getCounter());
    }
}
