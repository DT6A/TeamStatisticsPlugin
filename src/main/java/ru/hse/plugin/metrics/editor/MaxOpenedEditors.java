package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;
import ru.hse.plugin.metrics.commons.services.EditorCountingService;

import java.util.Objects;

import static java.lang.Math.max;
import static ru.hse.plugin.metrics.commons.util.Names.MAX_OPENED_EDITORS;

public class MaxOpenedEditors extends Metric {
    private int maximum = 0;

    public MaxOpenedEditors() { }

    public MaxOpenedEditors(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public void editorCreate(@NotNull Editor editor) {
        maximum = max(maximum, ServiceManager.getService(EditorCountingService.class).getCounter());
    }

    @Override
    public void clear() { }

    @Override
    public String getInfo() {
        return Integer.toString(maximum);
    }

    @Override
    public String toString() {
        return MAX_OPENED_EDITORS + " " + maximum;
    }

    @Override
    @NotNull
    public String getName() {
        return MAX_OPENED_EDITORS + "()";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        MaxOpenedEditors that = cast(metric, MaxOpenedEditors.class);

        maximum = max(this.maximum, that.maximum);

        that.clear();
    }

    @Override
    @NotNull
    public MetricJComponentWrapper makeComponent(Metric additional) {
        MaxOpenedEditors that = cast(additional, MaxOpenedEditors.class);
        return new CounterJComponentWrapper() {@Override
        protected int count() {
            return max(MaxOpenedEditors.this.maximum, that.maximum);
        }
        };
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Maximum number of opened editors";
    }

    @Override
    public boolean isSame(@NotNull Metric metric) {
        return metric.getClass() == getClass();
    }

    @Override
    public int hashSame() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaxOpenedEditors that = (MaxOpenedEditors) o;
        return maximum == that.maximum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximum);
    }
}
