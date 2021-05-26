package ru.hse.plugin.signin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

public class ProjectOpenCloseListener implements ProjectManagerListener {
    @Override
    public void projectOpened(@NotNull Project project) {
//        System.out.println("fuck it opened");
        LoginDialogHandler.tryLogin();
    }

    @Override
    public void projectClosed(@NotNull Project project) {
//        System.out.println("fuck it closed");
    }
}
