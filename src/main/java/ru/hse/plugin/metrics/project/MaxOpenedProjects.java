package ru.hse.plugin.metrics.project;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;
import ru.hse.plugin.metrics.commons.services.ProjectCountingService;

import java.util.Objects;

import static java.lang.Math.max;
import static ru.hse.plugin.metrics.commons.Names.MAX_OPENED_PROJECTS;

public class MaxOpenedProjects extends Metric {
    private int maximum = 0;

    public MaxOpenedProjects() { }

    public MaxOpenedProjects(int maximum) {
        this.maximum = maximum;
    }

    @Override
    public void updateProjectOpen(@NotNull Project project) {
        maximum = max(maximum, ServiceManager.getService(ProjectCountingService.class).getCounter());
    }

    @Override
    public void clear() { }

    @Override
    public String getInfo() {
        return Integer.toString(maximum);
    }

    @Override
    public String toString() {
        return MAX_OPENED_PROJECTS + " " + maximum;
    }

    @Override
    @NotNull
    public String getName() {
        return MAX_OPENED_PROJECTS + "()";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        MaxOpenedProjects that = cast(metric, MaxOpenedProjects.class);

        maximum = max(this.maximum, that.maximum);

        that.clear();
    }

    @Override
    @NotNull
    public MetricJComponentWrapper makeComponent(Metric additional) {
        MaxOpenedProjects that = cast(additional, MaxOpenedProjects.class);
        return new CounterJComponentWrapper() {@Override
            protected int count() {
                return max(MaxOpenedProjects.this.maximum, that.maximum);
            }
        };
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Maximum number of opened projects";
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
        MaxOpenedProjects that = (MaxOpenedProjects) o;
        return maximum == that.maximum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximum);
    }
}
