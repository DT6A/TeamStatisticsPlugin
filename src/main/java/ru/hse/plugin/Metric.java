package ru.hse.plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public interface Metric {

    // TODO почему boolean?
    boolean update(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file);

    void clear();

    String getInfo();

    @Override
    String toString(); // TODO хз как явно говорить что это обязательно надо заовеерайдить

    static Metric fromString(String metric) {
        String[] parts = metric.split(" ");
        if (parts[0].equals(PluginConstants.WORD_COUNTER)) {
            if (parts.length != 3) throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
            return new WordCounter(parts[1], Integer.parseInt(parts[2]));
        } else { // Все остальные чекеры тут будут тоже
            throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
        }
    }
}
