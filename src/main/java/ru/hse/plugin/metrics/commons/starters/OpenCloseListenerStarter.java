package ru.hse.plugin.metrics.commons.starters;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.commons.services.ProjectCountingService;
import ru.hse.plugin.storage.StorageData;

import java.nio.file.Paths;
import java.util.List;

public class OpenCloseListenerStarter implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        String path = project.getBasePath();

        if (path == null) {
            return;
        }

        ListenerStarter.addGit(Paths.get(path));
    }


    @Override
    public void projectClosed(@NotNull Project project) {
        String path = project.getBasePath();

        if (path == null) {
            return;
        }

        ListenerStarter.removeGit(Paths.get(path));
    }
}
