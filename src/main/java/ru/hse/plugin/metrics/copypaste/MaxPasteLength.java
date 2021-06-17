package ru.hse.plugin.metrics.copypaste;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.MaxMetric;

import static ru.hse.plugin.metrics.commons.util.Names.MAX_PASTE_LENGTH;

public class MaxPasteLength extends MaxMetric {
    @Override
    protected @NotNull String getClassName() {
        return MAX_PASTE_LENGTH;
    }

    public MaxPasteLength() {
        super();
    }

    public MaxPasteLength(int maximum) {
        super(maximum);
    }

    @Override
    public void paste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        updateMax(text.length());
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Maximum paste length";
    }

    @Override
    @NotNull
    public MaxPasteLength copy() {
        return new MaxPasteLength(getMaximum());
    }
}
