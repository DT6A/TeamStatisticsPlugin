package ru.hse.plugin.metrics.copypaste;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.PASTE_COUNTER;

public class PasteCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return PASTE_COUNTER;
    }

    public PasteCounter() {
        super();
    }

    public PasteCounter(int counter) {
        super(counter);
    }

    @Override
    public void paste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        inc();
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of pastes from clipboard";
    }

    @Override
    @NotNull
    public PasteCounter copy() {
        return new PasteCounter(getCounter());
    }
}
