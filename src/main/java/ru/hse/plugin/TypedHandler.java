package ru.hse.plugin;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class TypedHandler extends TypedHandlerDelegate {

    @NotNull
    @Override
    public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
//        StorageData storageData = ServiceManager.getService(StorageData.class);
//        synchronized (StorageData.class) {
//            StorageData.getInstance().inc();
//        }
//        Messages.showInfoMessage("Nice cock!!!!!!!!!", "ATTENTiON");
//        storageData.inc();
//        if (doc.getText().toLowerCase().contains("cock")) {
//            Messages.showInfoMessage("Nice cock!!!!!!!!!", "ATTENTiON");
//        }
        return Result.STOP;
    }

}