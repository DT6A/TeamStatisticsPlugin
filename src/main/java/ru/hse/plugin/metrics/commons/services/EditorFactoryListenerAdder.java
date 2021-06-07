package ru.hse.plugin.metrics.commons.services;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.storage.StorageData;

import java.util.List;

public class EditorFactoryListenerAdder implements Disposable {
    private boolean isAdded = false;

    public synchronized void tryAdd() {
        if (isAdded) return;

        EditorFactory editorFactory = EditorFactory.getInstance();

        Disposable disposable = Disposer.newDisposable();
        Disposer.register(this, disposable);

        editorFactory.addEditorFactoryListener(
                new EditorFactoryListener() {
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
                },
                disposable
        );

        isAdded = true;
    }

    @Override
    public void dispose() { }
}
