package ru.hse.plugin.metrics.commons.util;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;

public class MetricWrapper {
    @NotNull
    private final Metric metric;

    private MetricWrapper(@NotNull Metric metric) {
        this.metric = metric;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MetricWrapper)) {
            return false;
        }
        MetricWrapper that = (MetricWrapper) o;
        return metric.isSame(that.unwrap());
    }

    @Override
    public int hashCode() {
        return metric.hashSame();
    }

    public static MetricWrapper wrap(@NotNull Metric metric) {
        return new MetricWrapper(metric);
    }

    @NotNull
    public Metric unwrap() {
        return metric;
    }
}
