package ru.hse.plugin.metrics.commons.services;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import ru.hse.plugin.metrics.commons.listeners.BackSpaceListener;

public class BackSpaceListenerAdder {
    private boolean isAdded = false;

    public synchronized void tryAdd() {
        if (isAdded) return;

        ActionManager actionManager = ActionManager.getInstance();
        EditorAction defaultBackspaceAction = (EditorAction) actionManager.getAction("EditorBackSpace");
        defaultBackspaceAction.setupHandler(new BackSpaceListener(defaultBackspaceAction.getHandler()));

        isAdded = true;
    }
}
