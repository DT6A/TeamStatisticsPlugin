package ru.hse.plugin.metrics.typed;

import org.jetbrains.annotations.NotNull;

import static ru.hse.plugin.metrics.commons.util.Names.LINE_COUNTER;

public class LineCounter extends CharCounter {

    public LineCounter(int counter) {
        super('\n', counter);
    }

    public LineCounter() {
        super('\n');
    }

    @Override
    protected @NotNull String getClassName() {
        return LINE_COUNTER;
    }

    @Override
    public @NotNull String getName() {
        return getClassName();
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of lines";
    }

    @Override
    @NotNull
    public LineCounter copy() {
        return new LineCounter(getCounter());
    }
}
