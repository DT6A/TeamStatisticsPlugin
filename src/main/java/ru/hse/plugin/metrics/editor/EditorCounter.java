package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;

import static ru.hse.plugin.metrics.commons.util.Names.EDITOR_COUNTER;

public class EditorCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return EDITOR_COUNTER;
    }

    public EditorCounter() {
        super();
    }

    public EditorCounter(int counter) {
        super(counter);
    }

    @Override
    public void editorOpen(@NotNull Editor editor) {
        inc();
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    public @NotNull String localStatisticString() {
        return "Number of editor opens";
    }
}
