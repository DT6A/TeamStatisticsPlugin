package ru.hse.plugin.converters;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.abstracts.Metric;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListMetricConverter extends Converter<List<Metric>> {

    private final static Converter<Metric> metricConverter = new MetricConverter();

    @Nullable
    @Override
    public List<Metric> fromString(@NotNull String value) {
        return Arrays.stream(value.split("\\$"))
                .map(metricConverter::fromString)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String toString(@NotNull List<Metric> value) {
        return value.stream()
                .map(metricConverter::toString)
                .collect(Collectors.joining("$"));
    }
}
