package ru.hse.plugin.common;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.signin.LoginDialogHandler;

public class ProjectOpenCloseListener implements ProjectManagerListener {

    /**
     * Invoked on project open.
     *
     * @param project opening project
     */
    @Override
    public void projectOpened(@NotNull Project project) {
//        System.out.println("fuck it opened");
        LoginDialogHandler.tryLogin();
    }

    /**
     * Invoked on project close.
     *
     * @param project closing project
     */
    @Override
    public void projectClosed(@NotNull Project project) {
//        System.out.println("fuck it closed");
    }

}