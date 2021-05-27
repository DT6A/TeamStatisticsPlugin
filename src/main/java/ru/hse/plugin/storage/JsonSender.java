package ru.hse.plugin.storage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import ru.hse.plugin.metrics.editor.CharCounter;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.editor.WordCounter;
import ru.hse.plugin.util.WeNeedNameException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonSender {
    private final URL url;

    public JsonSender(@NotNull URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public boolean sendMetricInfo(byte[] out) {
        try {
            HttpURLConnection http = createHttpURLConnection();
            return sendData(http, out);
        }
        catch (IOException e) {
            return false;
        }
    }

    public String submitUserInfo(byte[] userInfo) throws WeNeedNameException {
        try {
            HttpURLConnection http = createHttpURLConnection();
            if (!sendData(http, userInfo)) {
                throw new WeNeedNameException("Cant submit user info");
            }
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String token;
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(http.getInputStream()))) {
                    String json = bufferedReader.lines().collect(Collectors.joining());
                    JSONObject obj = new JSONObject(json);
                    token = obj.getString("token");
                } catch (JSONException e) {
                    throw new WeNeedNameException("There are no token in JSON", e);
                }
                return token;
            } else {
                throw new WeNeedNameException("Cant submit user info");
            }
        }
        catch (IOException e) {
            throw new WeNeedNameException("Cant submit user info", e);
        }
    }

    public Set<Metric> getNewMetrics() {
        Set<Metric> metrics = new HashSet<>();
        try {
            HttpURLConnection http = createHttpURLConnection();
            http.setRequestMethod("GET");
            if (http.getResponseCode() == 200) {
                http.disconnect();
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8)
                )){
                    String json = bufferedReader.lines().collect(Collectors.joining());
                    JSONObject obj = new JSONObject(json);
                    JSONArray charCounting = obj.getJSONArray("CHAR_COUNTING");
                    JSONArray wordCounting = obj.getJSONArray("SUBSTRING_COUNTING");
                    for (int i = 0; i < charCounting.length(); i++) {
                        char character = charCounting.getString(i).charAt(0);
                        CharCounter charCounter = new CharCounter(character);
                        metrics.add(charCounter);
                    }
                    for (int i = 0; i < wordCounting.length(); i++) {
                        String word = wordCounting.getString(i);
                        WordCounter wordCounter = new WordCounter(word);
                        metrics.add(wordCounter);
                    }
                }
                return metrics;
            }
        } catch (IOException e) {
            return metrics;
        }
        return null;
    }

    private boolean sendData(HttpURLConnection http, byte[] out) {
        try {
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            http.connect();
            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
                os.flush();
            }
            return http.getResponseCode() == HttpURLConnection.HTTP_OK;
        }
        catch (IOException e) {
            return false;
        }
    }

    private HttpURLConnection createHttpURLConnection() throws IOException {
        URLConnection con = url.openConnection();
        return (HttpURLConnection) con;
    }
}
