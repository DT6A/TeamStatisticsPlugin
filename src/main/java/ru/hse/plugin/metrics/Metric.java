package ru.hse.plugin.metrics;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.component.MetricJComponentWrapper;
import ru.hse.plugin.util.PluginConstants;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class Metric {

    public abstract void update(
            char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file
    );

    public abstract void clear();

    public abstract String getInfo();

    @Override
    public abstract String toString();

    @NotNull
    public abstract String getName(); // Формат: 'ИмяКласс(поля, какого, то, конструктора)'

    /**
     * @param metric - метрика (metric is instance getClass()), которую
     *               1) `добавим` к имеющейся метрике
     *               2) вызовем metric.clear()
     * @throws RuntimeException если метрика не кастуется к нашему классу или они не ~равны
     *                                  (если например отслеживаем разные слова)
     */
    public abstract void mergeAndClear(Metric metric);

    public abstract MetricJComponentWrapper makeComponent(Metric additional);

    @NotNull
    public abstract String localStatisticString();

    protected <T> T cast(Object metric, Class<T> clazz) {
        if (!clazz.isInstance(metric)) {
            return clazz.cast(metric);
        } else {
            throw new RuntimeException("Argument metric has to be instance of " + clazz.getSimpleName());
        }
    }

    @Nullable
    public static Metric fromString(@Nullable String metric) {
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
