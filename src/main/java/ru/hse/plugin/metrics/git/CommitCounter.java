package ru.hse.plugin.metrics.git;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.COMMIT_COUNTER;

public class CommitCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return COMMIT_COUNTER;
    }

    public CommitCounter() {
        super();
    }

    public CommitCounter(int counter) {
        super(counter);
    }

    @Override
    public void justCommitted() {
        inc();
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of commits";
    }
}
