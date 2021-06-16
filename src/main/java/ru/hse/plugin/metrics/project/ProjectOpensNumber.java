package ru.hse.plugin.metrics.project;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.PROJECT_OPENS_NUMBER;

public class ProjectOpensNumber extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return PROJECT_OPENS_NUMBER;
    }

    public ProjectOpensNumber() {
        super();
    }

    public ProjectOpensNumber(int counter) {
        super(counter);
    }

    @Override
    public void updateProjectOpen(@NotNull Project project) {
        inc();
    }

    @Override
    @NotNull
    public String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of project opens";
    }

    @Override
    @NotNull
    public ProjectOpensNumber copy() {
        return new ProjectOpensNumber(getCounter());
    }
}
