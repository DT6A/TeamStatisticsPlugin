package ru.hse.plugin.converters;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.Metric;

public class MetricConverter extends Converter<Metric> {
    @Override
    public @Nullable Metric fromString(@Nullable @NonNls String s) {
        return Metric.fromString(s);
    }

    @Override
    public @Nullable String toString(@Nullable Metric metric) {
        if (metric == null) {
            return null;
        }

        return metric.toString();
    }
}
