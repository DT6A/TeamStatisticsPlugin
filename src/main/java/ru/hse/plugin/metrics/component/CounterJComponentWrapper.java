package ru.hse.plugin.metrics.component;

import ru.hse.plugin.util.Util;

import javax.swing.*;

public abstract class CounterJComponentWrapper extends MetricJComponentWrapper {
    private final JLabel label = Util.makeDefaultLabel(String.valueOf(count()));

    @Override
    public JComponent getComponent() {
        return label;
    }

    @Override
    public void update() {
        label.setText(String.valueOf(count()));
    }

    protected abstract int count();
}
