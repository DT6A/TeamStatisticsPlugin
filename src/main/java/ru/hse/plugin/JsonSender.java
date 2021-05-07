package ru.hse.plugin;

import java.net.URL;

public class JsonSender {
    private URL url;

    public JsonSender(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
}
