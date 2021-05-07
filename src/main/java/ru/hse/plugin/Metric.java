package ru.hse.plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public interface Metric {

    boolean update(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file);

    String getInfo();
}
