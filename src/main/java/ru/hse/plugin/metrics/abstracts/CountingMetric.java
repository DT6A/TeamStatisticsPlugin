package ru.hse.plugin.metrics.abstracts;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;

import java.util.Objects;

public abstract class CountingMetric extends Metric {
    @NotNull
    protected abstract String getClassName();

    private int counter = 0;

    protected CountingMetric() {
        System.out.println("Counter is 0 cuz def cons called");
    }

    protected CountingMetric(int counter) {
        System.out.println("Counter is " + counter);
        this.counter = counter;
    }

    protected void inc(int diff) {
        counter += diff;
    }

    protected void inc() {
        inc(1);
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

        if (!this.isSame(metric)) {
            throw new RuntimeException("Metrics are expected to be same");
        }

        this.counter += that.counter;

        that.clear();
    }

    protected int getCounter() {
        return counter;
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
    public boolean isSame(@NotNull Metric metric) {
        return metric.getClass() == getClass();
    }

    @Override
    public int hashSame() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CountingMetric that = (CountingMetric) o;
        return counter == that.counter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(counter);
    }
}
