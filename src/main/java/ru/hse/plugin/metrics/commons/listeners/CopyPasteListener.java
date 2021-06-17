package ru.hse.plugin.metrics.commons.listeners;

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RawText;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class CopyPasteListener implements CopyPastePreProcessor {
    @Nullable
    @Override
    public String preprocessOnCopy(PsiFile file, int[] startOffsets, int[] endOffsets, String text) {
        if (StorageData.getInstance().doNotCollectAndSendInformation) {
            return text;
        }

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.copy(file, startOffsets, endOffsets, text);
        }

        return text;
    }

    @NotNull
    @Override
    public String preprocessOnPaste(Project project, PsiFile file, Editor editor, String text, RawText rawText) {
        if (StorageData.getInstance().doNotCollectAndSendInformation) {
            return text;
        }

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.paste(project, file, editor, text, rawText);
        }

        return text;
    }
}
