package ru.hse.plugin.networking;

import java.net.MalformedURLException;
import java.net.URL;

public class URLs {
    public static final String SERVERS_URL = "http://127.0.0.1:8000/";
    public static final URL POST_URL;
    public static final URL LOGIN_URL;
    public static final URL PLUGIN_GET_METRICS_URL;

    static {
        try {
            POST_URL = new URL(SERVERS_URL + "post/");
            LOGIN_URL = new URL(SERVERS_URL + "plugin_login/");
            PLUGIN_GET_METRICS_URL = new URL(SERVERS_URL + "plugin_get_all_metrics/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
