package ru.hse.plugin.metrics.commons.listeners;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class GitListener extends GitListenerBase {
    protected GitListener(Path location) {
        super(location);
    }

    @NotNull
    @Override
    protected Runnable getCommitHandler() {
        return () -> {
            throw new UnsupportedOperationException();
        };
    }
}
