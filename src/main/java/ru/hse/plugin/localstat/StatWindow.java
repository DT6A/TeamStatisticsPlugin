package ru.hse.plugin.localstat;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.ui.FormBuilder;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;
import ru.hse.plugin.storage.StorageData;
import ru.hse.plugin.util.Util;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatWindow {
    private final JPanel panel = new JPanel();
    private final List<MetricJComponentWrapper> componentWrappers = new ArrayList<>();
    private final JButton updateButton = new JButton("Update");

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
    private final JLabel lastUpdateOn = Util.makeDefaultLabel(makeLastUpdatedOn());

    {
        updateButton.addActionListener(e -> {
            var built = build();
            panel.removeAll();
            panel.add(built);
            componentWrappers.forEach(MetricJComponentWrapper::update);
            lastUpdateOn.setText(makeLastUpdatedOn());
        });
    }

    /*
     * непонятно, лучше обновлять раз в четверть секунды очередным демоном
     *            лучше обновлять раз в ~10 секунд очередным демоном и оставить кнопку
     *            лучше оставить чисто кнопку
     */

    public StatWindow(ToolWindow toolWindow) {

    }

    public JComponent getContent() {
        panel.removeAll();
        panel.add(build());
        return panel;
    }

    private JComponent build() {
        componentWrappers.clear();

        var builder = FormBuilder.createFormBuilder();

        int size = StorageData.getInstance().accumulated.size();

        for (int i = 0; i < size; i++) {
            var accumulated = StorageData.getInstance().accumulated.get(i);
            var diff = StorageData.getInstance().diffs.get(i);

            var wrapper = accumulated.makeComponent(diff);
            componentWrappers.add(wrapper);

            builder.addLabeledComponent(accumulated.localStatisticString() + ":", wrapper.getComponent());
        }

        builder.addComponent(
                FormBuilder
                        .createFormBuilder()
                        .addLabeledComponent(lastUpdateOn, updateButton)
                        .getPanel()
        );

        builder.addComponentFillVertically(new JPanel(), 0);

        return builder.getPanel();
    }

    private static String makeLastUpdatedOn() {
        return "Last updated on " + dateFormatter.format(new Date());
    }
}
