package ru.hse.plugin.networking;

import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {

    private static final ExecutorService reader = Executors.newSingleThreadExecutor(new DaemonThreadFactory());

    private static final ExecutorService writer = Executors.newSingleThreadExecutor(new DaemonThreadFactory());

    public static void updateMetrics() {
        reader.submit(() -> {
            var instance = StorageData.getInstance();

            Set<Metric> currentServerMetrics = instance.jsonSender.getNewMetrics(
                    instance.userInfo.getTokenNoExcept()
            );
            if (currentServerMetrics != null) {
                var currentPluginMetrics = instance.metrics();
                for (Metric metric : currentServerMetrics) {
                    if (!instance.metrics().contains(metric)) {
                        // TODO make clone
                        instance.metrics().add(metric);
                        instance.diffs.add(metric);
                        instance.accumulated.add(metric);
                    }
                }
                currentPluginMetrics.removeIf(currentServerMetrics::contains);
                instance.accumulated.removeIf(currentServerMetrics::contains);
                instance.diffs.removeIf(currentServerMetrics::contains);
            }
        });
    }

    public static void sendMetrics() {
        writer.submit(() -> {
            var instance = StorageData.getInstance();
            if (instance.jsonSender.sendMetricInfo(
                    instance.getMetricsInfo(),
                    instance.userInfo.getTokenNoExcept())) {
                instance.clearMetrics();
            }
        });
    }

}
