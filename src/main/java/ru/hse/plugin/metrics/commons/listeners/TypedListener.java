package ru.hse.plugin.metrics.commons.listeners;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class TypedListener extends TypedHandlerDelegate {

    @Override
    public boolean isImmediatePaintingEnabled(@NotNull Editor editor, char c, @NotNull DataContext context) {
        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.updateBeforeCharTyped(c, editor);
        }
        return super.isImmediatePaintingEnabled(editor, c, context);
    }

//    @NotNull
//    @Override
//    public Result beforeCharTyped(
//            char c,
//            @NotNull Project project,
//            @NotNull Editor editor,
//            @NotNull PsiFile file,
//            @NotNull FileType fileType) {
//        List<Metric> metrics = StorageData.getInstance().diffs;
//        for (Metric metric : metrics) {
//            metric.updateBeforeCharTyped(c, editor);
//        }
//        return Result.CONTINUE;
//    }

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.updateCharTyped(c, project, editor, file);
        }
        return Result.CONTINUE;
    }

}
