package ru.hse.plugin.metrics.commons.component;

import javax.swing.*;

public abstract class MetricJComponentWrapper {
    public abstract JComponent getComponent();
    public abstract void update();
}
