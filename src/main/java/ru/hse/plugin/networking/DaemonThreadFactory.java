package ru.hse.plugin.networking;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    }
}
