package ru.hse.plugin.metrics.commons.starters;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public class OpenCloseListenerStarter implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
        String path = project.getBasePath();

        if (path == null) {
            return;
        }

        ListenerStarter.addGit(Paths.get(path));
        ListenerStarter.startEditorCount();
        ListenerStarter.startBackSpaceCounter();
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
