package ru.hse.plugin.metrics.copypaste;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.PASTE_LENGTH_COUNTER;

public class PasteLengthCounter extends CountingMetric { // TODO test + add to parser
    @NotNull
    @Override
    protected String getClassName() {
        return PASTE_LENGTH_COUNTER;
    }

    public PasteLengthCounter() {
        super();
    }

    public PasteLengthCounter(int counter) {
        super(counter);
    }

    @Override
    public void paste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        inc(text.length());
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Length of pastes from clipboard";
    }
}
