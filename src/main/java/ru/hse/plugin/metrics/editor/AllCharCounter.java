package ru.hse.plugin.metrics.editor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.localstat.AllSymbolStatistics;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.component.MetricJComponentWrapper;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.hse.plugin.metrics.commons.Names.ALL_CHAR_COUNTER;

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
        return ALL_CHAR_COUNTER + "()";
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
    public boolean isSame(@NotNull Metric metric) {
        return metric.getClass() == getClass();
    }

    @Override
    public int hashSame() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        var sj = new StringJoiner(" ").add(ALL_CHAR_COUNTER);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllCharCounter that = (AllCharCounter) o;
        return chars.equals(that.chars);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chars);
    }
}
