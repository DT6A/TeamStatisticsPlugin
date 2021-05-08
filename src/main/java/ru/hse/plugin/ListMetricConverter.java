package ru.hse.plugin;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListMetricConverter extends Converter<List<Metric>> {
    @Nullable
    @Override
    public List<Metric> fromString(@NotNull String value) {
        final Converter<Metric> metricConverter = new MetricConverter(); // TODO mb static? but mb leaks cuz of jb
        return Arrays.stream(value.split("\\$"))
                .map(metricConverter::fromString)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String toString(@NotNull List<Metric> value) {
        final Converter<Metric> metricConverter = new MetricConverter();
        return value.stream()
                .map(metricConverter::toString)
                .collect(Collectors.joining("$"));
    }
}
