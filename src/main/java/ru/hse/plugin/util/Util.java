package ru.hse.plugin.util;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Util {
    private Util() {}

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
}
