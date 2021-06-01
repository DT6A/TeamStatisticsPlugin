package ru.hse.plugin.metrics.commons.services;

import ru.hse.plugin.metrics.commons.listeners.GitListener;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static ru.hse.plugin.util.Constants.GIT_CHECKS_PER_JUST;
import static ru.hse.plugin.util.Constants.GIT_JUST_MILLISECONDS;
import static ru.hse.plugin.util.Util.canonical;

public class GitListeningService {
    private final Map<Path, GitListener> listeners = new ConcurrentHashMap<>();
    private final Thread checker = new Thread(() -> {
        try {
            while (!Thread.interrupted()) {
                for (GitListener gitListener : listeners.values()) {
                    gitListener.tryAll();
                }

                TimeUnit.MILLISECONDS.sleep((long) (GIT_JUST_MILLISECONDS * GIT_CHECKS_PER_JUST));
            }
        } catch (InterruptedException | IOException ignored) { }
    });

    {
        checker.setDaemon(true);
    }

    public void tryStart() {
        if (checker.getState() == Thread.State.NEW) {
            checker.start();
        }
    }

    public void add(Path path) {
        path = canonical(path);
        listeners.computeIfAbsent(path, GitListener::new);
    }

    public void remove(Path path) {
        path = canonical(path);
        listeners.remove(path);
    }
}
