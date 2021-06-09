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
        return o instanceof Metric && metric.isSame((Metric) o);
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
