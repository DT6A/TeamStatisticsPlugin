package ru.hse.plugin.metrics.abstracts;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.commons.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;

import java.util.Objects;

import static java.lang.Math.max;

public abstract class MaxMetric extends Metric {
    @NotNull
    protected abstract String getClassName();

    private int maximum;

    protected int getMaximum() {
        return maximum;
    }

    public MaxMetric() {
        this(0);
    }

    public MaxMetric(int maximum) {
        this.maximum = maximum;
    }

    protected void updateMax(int currentValue) {
        maximum = max(maximum, currentValue);
    }

    @Override
    public void clear() { }

    @Override
    public String getInfo() {
        return Integer.toString(maximum);
    }

    @Override
    public String toString() {
        return getClassName() + " " + maximum;
    }

    @Override
    @NotNull
    public String getName() {
        return getClassName() + "()";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        MaxMetric that = cast(metric, MaxMetric.class);

        maximum = max(this.maximum, that.maximum);

        that.clear();
    }

    @Override
    @NotNull
    public MetricJComponentWrapper makeComponent(Metric additional) {
        MaxMetric that = cast(additional, MaxMetric.class);
        return new CounterJComponentWrapper() {
            @Override
            protected int count() {
                return max(MaxMetric.this.maximum, that.maximum);
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
        MaxMetric that = (MaxMetric) o;
        return maximum == that.maximum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maximum);
    }
}
