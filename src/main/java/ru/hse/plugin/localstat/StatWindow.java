package ru.hse.plugin.localstat;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.util.ui.FormBuilder;
import ru.hse.plugin.metrics.component.MetricJComponentWrapper;
import ru.hse.plugin.storage.StorageData;
import ru.hse.plugin.util.Util;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatWindow {
    private final JPanel panel;
    private final List<MetricJComponentWrapper> componentWrappers = new ArrayList<>();
    private final JButton updateButton = new JButton("Update");

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
    private final JLabel lastUpdateOn = Util.makeDefaultLabel(SPACES + makeLastUpdatedOn());

    {
        updateButton.addActionListener(e -> {
            componentWrappers.forEach(MetricJComponentWrapper::update);
            lastUpdateOn.setText(SPACES + makeLastUpdatedOn());
        });
    }

    /*
     * непонятно, лучше обновлять раз в четверть секунды очередным демоном
     *            лучше обновлять раз в ~10 секунд очередным демоном и оставить кнопку
     *            лучше оставить чисто кнопку
     */

    public StatWindow(ToolWindow toolWindow) {
        var builder = FormBuilder.createFormBuilder();

        int size = StorageData.getInstance().accumulated.size();

        for (int i = 0; i < size; i++) {
            var accumulated = StorageData.getInstance().accumulated.get(i);
            var diff = StorageData.getInstance().metrics.get(i);

            var wrapper = accumulated.makeComponent(diff);
            componentWrappers.add(wrapper);

            builder.addLabeledComponent(SPACES + accumulated.localStatisticString() + ":", wrapper.getComponent());
        }

        builder.addComponent(
                FormBuilder
                        .createFormBuilder()
                        .addLabeledComponent(lastUpdateOn, updateButton)
                        .getPanel()
        );

        builder.addComponentFillVertically(new JPanel(), 0);

        panel = builder.getPanel();
    }

    public JComponent getContent() {
        return panel;
    }

    private static final String SPACES = "    ";

    private static String makeLastUpdatedOn() {
        return "Last updated on " + dateFormatter.format(new Date());
    }
}
