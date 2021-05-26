package ru.hse.plugin.metrics;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.component.MetricJComponentWrapper;
import ru.hse.plugin.metrics.services.ProjectCountingService;
import ru.hse.plugin.util.PluginConstants;

import static java.lang.Math.max;

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
        return PluginConstants.MAX_OPENED_PROJECTS + " " + maximum;
    }

    @Override
    @NotNull
    public String getName() {
        return PluginConstants.MAX_OPENED_PROJECTS + "()";
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
}
