package ru.hse.plugin.metrics.commons.services;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.util.Disposer;
import ru.hse.plugin.metrics.commons.listeners.MyEditorFactoryListener;

public class EditorFactoryListenerAdder implements Disposable {
    private boolean isAdded = false;

    public synchronized void tryAdd() {
        if (isAdded) return;

        EditorFactory editorFactory = EditorFactory.getInstance();

        Disposable disposable = Disposer.newDisposable();
        Disposer.register(this, disposable);

        editorFactory.addEditorFactoryListener(
                new MyEditorFactoryListener(),
                disposable
        );

        isAdded = true;
    }

    @Override
    public void dispose() { }
}
