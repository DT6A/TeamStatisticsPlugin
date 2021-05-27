package ru.hse.plugin.metrics.commons.listeners;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.commons.services.ProjectCountingService;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class ProjectOpenCloseListener implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        ProjectCountingService counter = ServiceManager.getService(ProjectCountingService.class);

        counter.inc();

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.updateProjectOpen(project);
        }
    }


    @Override
    public void projectClosed(@NotNull Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        ProjectCountingService projectCountingService = ServiceManager.getService(ProjectCountingService.class);

        projectCountingService.dec();

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.updateProjectClose(project);
        }
    }

}
