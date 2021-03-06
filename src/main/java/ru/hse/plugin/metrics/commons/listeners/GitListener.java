package ru.hse.plugin.metrics.commons.listeners;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class GitListener extends GitListenerBase {
    public GitListener(Path location) {
        super(location);
    }

    @NotNull
    @Override
    protected Runnable getCommitHandler() {
        return () -> {
            if (StorageData.getInstance().doNotCollectAndSendInformation) {
                return;
            }

            List<Metric> metrics = StorageData.getInstance().diffs;
            for (Metric metric : metrics) {
                metric.justCommitted();
            }
        };
    }

    @Override
    protected @NotNull Consumer<String> getCommitOnBranchHandler() {
        return name -> {
            if (StorageData.getInstance().doNotCollectAndSendInformation) {
                return;
            }

            List<Metric> metrics = StorageData.getInstance().diffs;
            for (Metric metric : metrics) {
                metric.justCommitted(name);
            }
        };
    }
}
