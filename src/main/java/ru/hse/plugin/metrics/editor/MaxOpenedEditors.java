package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.MaxMetric;
import ru.hse.plugin.metrics.commons.services.EditorCountingService;

import static ru.hse.plugin.metrics.commons.util.Names.MAX_OPENED_EDITORS;

public class MaxOpenedEditors extends MaxMetric {
    @Override
    protected @NotNull String getClassName() {
        return MAX_OPENED_EDITORS;
    }

    public MaxOpenedEditors() {
        super();
    }

    public MaxOpenedEditors(int maximum) {
        super(maximum);
    }

    @Override
    public void editorCreate(@NotNull Editor editor) {
        updateMax(ServiceManager.getService(EditorCountingService.class).getCounter());
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Maximum number of opened editors";
    }
}
