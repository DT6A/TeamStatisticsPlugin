package ru.hse.plugin.metrics.abstracts;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;
import ru.hse.plugin.metrics.commons.util.ConvertedMetricParser;

public abstract class Metric {

    public void updateCharTyped(
            char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file
    ) { }

    public void updateProjectOpen(@NotNull Project project) { }
    public void updateProjectClose(@NotNull Project project) { }

    public void justCommitted() { }
    public void justCommitted(String branchName) { }

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

    @NotNull
    public abstract MetricJComponentWrapper makeComponent(Metric additional);
    @NotNull
    public abstract String localStatisticString();

    public abstract boolean isSame(@NotNull Metric metric);
    public abstract int hashSame();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    protected <T> T cast(Object metric, Class<T> clazz) {
        if (clazz.isInstance(metric)) {
            return clazz.cast(metric);
        } else {
            throw new RuntimeException("Argument metric has to be instance of " + clazz.getSimpleName());
        }
    }

    @Nullable
    public static Metric fromString(@Nullable String metric) {
        return ConvertedMetricParser.parse(metric);
    }
}
