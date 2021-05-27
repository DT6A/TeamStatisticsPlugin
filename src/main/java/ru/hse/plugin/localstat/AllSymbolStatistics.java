package ru.hse.plugin.localstat;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.editor.AllCharCounter;
import ru.hse.plugin.util.Pair;
import ru.hse.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.function.ToIntFunction;

public class AllSymbolStatistics extends DialogWrapper {
    @NotNull private final AllCharCounter main;
    @NotNull private final AllCharCounter additional;

    protected AllSymbolStatistics(@NotNull AllCharCounter main, @NotNull AllCharCounter additional) {
        super(true);
        this.main = main;
        this.additional = additional;
        setTitle("All Symbol Statistics");
        init();
    }

    public static void show(@NotNull AllCharCounter main, @NotNull AllCharCounter additional) {
        new AllSymbolStatistics(main, additional).show();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        final var gridBag = new GridBag()
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP));

        JPanel panel = new JPanel(new GridBagLayout());


        Util.zipWith(
                main.getChars().entrySet().stream(),
                additional.getChars().entrySet().stream(),
                (e1, e2) -> {
                    if (e1.getKey() != e2.getKey()) {
                        throw new RuntimeException("Entries are supposed to be with same key");
                    }
                    return Pair.of(e1.getKey(), e1.getValue() + e2.getValue());
                }
                )
                .filter(p -> p.getSecond() != 0)
                .sorted(
                        Comparator.comparingInt(
                                (ToIntFunction<Pair<Character, Integer>>) Pair::getSecond
                        ).reversed()
                )
                .forEach(pair -> {
                    panel.add(
                            Util.makeDefaultLabel(String.valueOf(pair.getFirst())),
                            gridBag.nextLine().next().weightx(0.5)
                    );

                    panel.add(
                            Util.makeDefaultLabel("#" + pair.getSecond()),
                            gridBag.next().weightx(0.5)
                    );

                    System.out.println(pair);
                });

        return panel;
    }
}
