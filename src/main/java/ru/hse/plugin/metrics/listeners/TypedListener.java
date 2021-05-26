package ru.hse.plugin.metrics.listeners;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class TypedListener extends TypedHandlerDelegate {

//    @NotNull
//    @Override
//    public Result beforeCharTyped(
//            char c,
//            @NotNull Project project,
//            @NotNull Editor editor,
//            @NotNull PsiFile file,
//            @NotNull FileType fileType) { // Эта штука, например, отлавливает символ '`', charTyped -- нет, вроде так ок
////        Messages.showInfoMessage("Nice cock!!!!!!!!!", "ATTENTiON");
//        List<Metric> metrics = StorageData.getInstance().metrics;
//        for (Metric metric : metrics) {
//            metric.update(project, editor, file);
//        }
//        return Result.CONTINUE;
//    }

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
//        Messages.showInfoMessage("Nice cock!!!!!!!!!", "ATTENTiON");
        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.updateCharTyped(c, project, editor, file);
        }
        return Result.CONTINUE;
    }

}