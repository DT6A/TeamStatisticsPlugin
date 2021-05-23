package ru.hse.plugin.converters;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.Metric;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SetMetricConverter extends Converter<Set<Metric>> {

    private final static Converter<Metric> metricConverter = new MetricConverter();

    @Override
    public @Nullable Set<Metric> fromString(@NotNull String value) {
        return Arrays.stream(value.split("\\$"))
                .map(metricConverter::fromString)
                .collect(Collectors.toSet());
    }

    @Override
    public @Nullable String toString(@NotNull Set<Metric> value) {
         return value.stream()
                .map(metricConverter::toString)
                .collect(Collectors.joining("$"));
    }
}
