package ru.hse.plugin.metrics.project;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.MaxMetric;
import ru.hse.plugin.metrics.commons.services.ProjectCountingService;

import static ru.hse.plugin.metrics.commons.util.Names.MAX_OPENED_PROJECTS;

public class MaxOpenedProjects extends MaxMetric {
    @Override
    protected @NotNull String getClassName() {
        return MAX_OPENED_PROJECTS;
    }

    public MaxOpenedProjects() {
        super();
    }

    public MaxOpenedProjects(int maximum) {
        super(maximum);
    }

    @Override
    public void updateProjectOpen(@NotNull Project project) {
        updateMax(ServiceManager.getService(ProjectCountingService.class).getCounter());
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Maximum number of opened projects";
    }

    @Override
    @NotNull
    public MaxOpenedProjects copy() {
        return new MaxOpenedProjects(getMaximum());
    }
}
