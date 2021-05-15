package ru.hse.plugin;

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
}
