package ru.hse.plugin.metrics.commons.listeners;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.services.EditorCountingService;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class MyEditorFactoryListener implements EditorFactoryListener {
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        EditorCountingService counter = ServiceManager.getService(EditorCountingService.class);
        counter.inc();

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.editorCreate(event.getEditor());
        }
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        EditorCountingService counter = ServiceManager.getService(EditorCountingService.class);
        counter.dec();

        List<Metric> metrics = StorageData.getInstance().diffs;
        for (Metric metric : metrics) {
            metric.editorRelease(event.getEditor());
        }
    }
}
