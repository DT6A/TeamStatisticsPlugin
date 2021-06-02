package ru.hse.plugin.metrics.abstracts;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;

public abstract class CountingMetric extends Metric {
    @NotNull
    protected abstract String getClassName();

    private int counter = 0;

    protected CountingMetric() { }

    protected CountingMetric(int counter) {
        this.counter = counter;
    }

    protected void inc() {
        counter++;
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
        return getClassName() + " " + counter;
    }

    @Override
    public void mergeAndClear(Metric metric) {
        CountingMetric that = cast(metric, getClass());

        if (!this.equals(metric)) {
            throw new RuntimeException("Metrics are expected to be same");
        }

        this.counter += that.counter;

        that.clear();
    }

    @NotNull
    @Override
    public MetricJComponentWrapper makeComponent(Metric additional) {
        CountingMetric that = cast(additional, getClass());
        return new CounterJComponentWrapper() {
            @Override
            protected int count() {
                return CountingMetric.this.counter + that.counter;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
