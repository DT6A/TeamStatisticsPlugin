package ru.hse.plugin.metrics.project;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;

import static ru.hse.plugin.metrics.commons.NAMES.PROJECT_OPENS_NUMBER;

public class ProjectOpensNumber extends Metric {
    private int counter = 0;

    public ProjectOpensNumber() { }

    public ProjectOpensNumber(int counter) {
        this.counter = counter;
    }

    @Override
    public void updateProjectOpen(@NotNull Project project) {
        counter++;
    }

    @Override
    public void clear() {
        counter = 0;
    }

    @Override
    public String getInfo() {
        return Integer.toString(counter);
    }

    @Override
    public String toString() {
        return PROJECT_OPENS_NUMBER + " " + counter;
    }

    @Override
    @NotNull
    public String getName() {
        return PROJECT_OPENS_NUMBER + "()";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        ProjectOpensNumber that = cast(metric, ProjectOpensNumber.class);

        this.counter += that.counter;

        that.clear();
    }

    @Override
    @NotNull
    public MetricJComponentWrapper makeComponent(Metric additional) {
        ProjectOpensNumber that = cast(additional, ProjectOpensNumber.class);
        return new CounterJComponentWrapper() {
            @Override
            protected int count() {
                return ProjectOpensNumber.this.counter + that.counter;
            }
        };
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of project opens";
    }
}
