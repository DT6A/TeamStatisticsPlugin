package ru.hse.plugin.metrics.backspaces;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.DELETION_COUNTER;

public class DeletionCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return DELETION_COUNTER;
    }

    public DeletionCounter() {
        super();
    }

    public DeletionCounter(int counter) {
        super(counter);
    }

    @Override
    public void backspaceDone() {
        inc();
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    @NotNull
    public String localStatisticString() {
        return "Number of deletions";
    }

    @Override
    @NotNull
    public DeletionCounter copy() {
        return new DeletionCounter(getCounter());
    }
}
