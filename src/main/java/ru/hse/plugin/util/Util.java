package ru.hse.plugin.util;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Util {
    private Util() {}

    @NotNull
    public static Path canonical(Path path) {
        return path.toAbsolutePath().normalize();
    }

    public static void openLink(String link) {
        URI uri;

        try {
            uri = new URI(link);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static JLabel makeDefaultLabel(String text) {
        final var label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.LARGE);
        label.setFontColor(UIUtil.FontColor.NORMAL);
        label.setBorder(JBUI.Borders.empty(5));
        return label;
    }

    public static JLabel makeSmallerLabel(String text) {
        final var label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(5));
        return label;
    }

    public static <A, B, C> Stream<C> zipWith(
            Stream<? extends A> first,
            Stream<? extends B> second,
            BiFunction<A, B, ? extends C> zipper
    ) {
        var spliterator1 = first.spliterator();
        var spliterator2 = second.spliterator();

        int characteristics = spliterator1.characteristics() & spliterator2.characteristics() &
                ~(Spliterator.DISTINCT | Spliterator.SORTED);

        long size = ((characteristics & Spliterator.SIZED) != 0)
                ? Math.min(spliterator1.getExactSizeIfKnown(), spliterator2.getExactSizeIfKnown())
                : -1;

        System.out.println("Before return");
        return StreamSupport.stream(
                Spliterators.spliterator(new Iterator<>() {
                         private final Iterator<A> iterator1 = Spliterators.iterator(spliterator1);
                         private final Iterator<B> iterator2 = Spliterators.iterator(spliterator2);

                         @Override
                         public boolean hasNext() {
                             return iterator1.hasNext() && iterator2.hasNext();
                         }

                         @Override
                         public C next() {
                            return zipper.apply(iterator1.next(), iterator2.next());
                        }
                    },
                    size,
                    characteristics
                ),
                first.isParallel() && second.isParallel()
        );
    }
}
