package ru.hse.plugin;

import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class JsonSender {
    private final URL url;

    public JsonSender(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public boolean sendData(@NotNull Map<String, String> nameToMetric) {
        // TODO Нормальный процесс обработки успеха/неудачи
        // TODO add user_id
        nameToMetric.put("user_id", "1");
        try {
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            System.out.println(JSONValue.toJSONString(nameToMetric));
            byte[] out = JSONValue.toJSONString(nameToMetric).getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
        }
        catch (IOException e) {
            return false;
        }
        return true;

    }
}
