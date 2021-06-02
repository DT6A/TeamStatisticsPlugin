package ru.hse.plugin.metrics.commons.starters;

import com.intellij.openapi.components.ServiceManager;
import ru.hse.plugin.metrics.commons.services.GitListeningService;

import java.nio.file.Path;

public class ListenerStarter {
    private ListenerStarter() { }

    public static void addGit(Path path) {
        startGit();
        addGitImpl(path);
    }

    public static void removeGit(Path path) {
        ServiceManager.getService(GitListeningService.class).remove(path);
    }

    private static void startGit() {
        ServiceManager.getService(GitListeningService.class).tryStart();
    }

    private static void addGitImpl(Path path) {
        ServiceManager.getService(GitListeningService.class).add(path);
    }
}
