package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.localstat.AllSymbolStatistics;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;
import ru.hse.plugin.util.PluginConstants;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AllCharCounter extends Metric {
    private final Map<Character, Integer> chars = new HashMap<>();

    public AllCharCounter() {}

    public AllCharCounter(List<Integer> counters) {
        var values = Stream.concat(
                IntStream.rangeClosed(0, 9).mapToObj(i -> Character.forDigit(i, 10)),
                IntStream.rangeClosed('a', 'z').mapToObj(i -> (char)i)
        ).collect(Collectors.toList());

        assert values.size() == counters.size();

        for (int i = 0; i < values.size(); i++) {
            chars.put(values.get(i), counters.get(i));
        }
    }

    @Override
    public void updateCharTyped(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (!Character.isLetterOrDigit(charTyped)) {
            return;
        }
        charTyped = Character.toLowerCase(charTyped);
        chars.computeIfPresent(charTyped, (c, i) -> i + 1);
        chars.putIfAbsent(charTyped, 1);
    }

    @Override
    public void clear() {
        chars.clear();
    }

    @Override
    public String getInfo() {
        return getCounters().toString();
    }

    @Override
    public @NotNull String getName() {
        return PluginConstants.ALL_CHAR_COUNTER + "()";
    }

    @Override
    public void mergeAndClear(Metric metric) {
        AllCharCounter that = cast(metric, getClass());

        for (var entry : that.chars.entrySet()) {
            this.chars.computeIfPresent(entry.getKey(), (c, i) -> i + entry.getValue());
            this.chars.computeIfAbsent(entry.getKey(), c -> entry.getValue());
        }

        that.clear();
    }

    @Override
    @NotNull
    public MetricJComponentWrapper makeComponent(Metric additional) {
        final var that = cast(additional, AllCharCounter.class);
        return new MetricJComponentWrapper() {
            private final JButton button = new JButton("Show");

            {
                button.addActionListener(e -> AllSymbolStatistics.show(AllCharCounter.this, that));
            }

            @Override
            public JComponent getComponent() {
                return button;
            }

            @Override
            public void update() { }
        };
    }

    @NotNull
    @Override
    public String localStatisticString() {
        return "All symbol counters";
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(" ").add(PluginConstants.ALL_CHAR_COUNTER);
        sj.merge(getCounters());
        return sj.toString();
    }

    public Map<Character, Integer> getChars() {
        return chars;
    }

    private StringJoiner getCounters() {
        var sj = new StringJoiner(" ");
        for (int i = 0; i <= 9; i++) {
            Integer counter = chars.get(Character.forDigit(i, 10));
            sj.add(counter == null ? "0" : counter.toString());
        }
        for (char c = 'a'; c <= 'z'; c++) {
            Integer counter = chars.get(c);
            sj.add(counter == null ? "0" : counter.toString());
        }
        return sj;
    }
}
