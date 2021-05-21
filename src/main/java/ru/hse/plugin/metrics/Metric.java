package ru.hse.plugin.metrics;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.util.PluginConstants;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface Metric {

    void update(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file);

    void clear();

    String getInfo();

    @Override
    String toString(); // TODO хз как явно говорить что это обязательно надо заовеерайдить

    @NotNull
    String getName(); // Формат: 'ИмяКласс(поля, какого, то, конструктора)'

    @Nullable
    static Metric fromString(@Nullable String metric) {
        if (metric == null) {
            return null;
        }

        String[] parts = metric.split(" ");

        switch (parts[0]) {
            case PluginConstants.WORD_COUNTER:
                if (parts.length != 3) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new WordCounter(parts[1], Integer.parseInt(parts[2]));
            case PluginConstants.CHAR_COUNTER:
                if (parts.length != 3) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new CharCounter((char) Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            case PluginConstants.ALL_CHAR_COUNTER:
                if (parts.length != 10 + ('z' - 'a' + 1) + 1) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new AllCharCounter(
                        Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList())
                );
            default:
                throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
        }
    }
}
