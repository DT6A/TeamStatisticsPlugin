package ru.hse.plugin.metrics.backspaces;

import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;
import ru.hse.plugin.metrics.editor.EditorCounter;

import static ru.hse.plugin.metrics.commons.util.Names.DELETED_LENGTH_COUNTER;

public class DeletedLengthCounter extends CountingMetric {
    @NotNull
    @Override
    protected String getClassName() {
        return DELETED_LENGTH_COUNTER;
    }

    public DeletedLengthCounter() {
        super();
    }

    public DeletedLengthCounter(int counter) {
        super(counter);
    }

    @Override
    public void backspaceLength(int length) {
        inc(length);
    }

    @Override
    public @NotNull String getName() {
        return getClassName() + "()";
    }

    @Override
    @NotNull
    public String localStatisticString() {
        return "Length of deleted data";
    }

    @Override
    @NotNull
    public DeletedLengthCounter copy() {
        return new DeletedLengthCounter(getCounter());
    }
}
