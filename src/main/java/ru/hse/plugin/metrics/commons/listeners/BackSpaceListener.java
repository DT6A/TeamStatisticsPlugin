package ru.hse.plugin.metrics.commons.listeners;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class BackSpaceListener extends EditorWriteActionHandler {
    private final EditorActionHandler originalHandler;

    public BackSpaceListener(EditorActionHandler originalHandler) {
        this.originalHandler = originalHandler;
    }

    @Override
    public void executeWriteAction(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        var selectedText = editor.getSelectionModel().getSelectedText();
        int length;
        if (selectedText != null) {
            length = selectedText.length();
        } else {
            length = 1;
        }

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.backspaceLength(length);
            metric.backspaceDone();
        }

        originalHandler.execute(editor, caret, dataContext);
    }
}
