package ru.hse.plugin.metrics.git;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;
import ru.hse.plugin.util.PluginConstants;

public class CommitCounter extends Metric { // FIXME: не запускается ниоткуда (листенер) И не тестилось
    private int counter = 0;

    public CommitCounter() { }

    public CommitCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public void clear() {
        counter = 0;
    }

    @Override
    public String getInfo() {
        return Integer.toString(counter);
    }

    @Override
    public String toString() {
        return PluginConstants.COMMIT_COUNTER + " " + counter;
    }

    @Override
    public @NotNull String getName() {
        return PluginConstants.COMMIT_COUNTER + "()";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        CommitCounter that = cast(metric, CommitCounter.class);

        this.counter += that.counter;

        that.clear();
    }

    @Override
    public @NotNull MetricJComponentWrapper makeComponent(Metric additional) {
        CommitCounter that = cast(additional, CommitCounter.class);
        return new CounterJComponentWrapper() {
            @Override
            protected int count() {
                return CommitCounter.this.counter + that.counter;
            }
        };
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of commits";
    }
}
