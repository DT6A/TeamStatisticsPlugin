package ru.hse.plugin;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class TypedHandler extends TypedHandlerDelegate {

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
    // TODO эта штука не про все символы говорит, прошлая говорит,
    //      но там проблема, что там document не видит изменения
    //      (очев из ее названия)
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
//        Messages.showInfoMessage("Nice cock!!!!!!!!!", "ATTENTiON");
        List<Metric> metrics = StorageData.getInstance().metrics;
        for (Metric metric : metrics) {
            metric.update(c, project, editor, file);
        }
        return Result.STOP;
    }

}