package ru.hse.plugin.metrics.component;

import javax.swing.*;

public abstract class MetricJComponentWrapper {
    public abstract JComponent getComponent();
    public abstract void update();
}
