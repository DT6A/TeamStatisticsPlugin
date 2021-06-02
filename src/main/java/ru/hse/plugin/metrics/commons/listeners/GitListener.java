package ru.hse.plugin.metrics.commons.listeners;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.nio.file.Path;
import java.util.List;

public class GitListener extends GitListenerBase {
    public GitListener(Path location) {
        super(location);
    }

    @NotNull
    @Override
    protected Runnable getCommitHandler() {
        return () -> {
            List<Metric> metrics = StorageData.getInstance().diffs;
            for (Metric metric : metrics) {
                metric.justCommitted();
            }
        };
    }
}
