package ru.hse.plugin;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

public class MetricConverter extends Converter<Metric> {
    @Override
    public @Nullable Metric fromString(@Nullable @NonNls String s) {
        return null;
    }

    @Override
    public @Nullable String toString(@Nullable Metric metric) {
        return null;
    }
}