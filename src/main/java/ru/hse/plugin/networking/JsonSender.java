package ru.hse.plugin.networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.metrics.editor.CharCounter;
import ru.hse.plugin.metrics.editor.WordCounter;
import ru.hse.plugin.storage.UserInfo;
import ru.hse.plugin.util.Serializer;
import ru.hse.plugin.util.WeNeedNameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonSender {

    private String lastSendingTime;

    public JsonSender() {
        updateSendingTimeToCurrent();
    }

    public JsonSender(String lastSendingTime) {
        this.lastSendingTime = lastSendingTime;
    }


    public boolean sendMetricInfo(Map<String, String> metricInfo,
                                  String token) {
        metricInfo.put("time_from", getLastSendingTime());
        updateSendingTimeToCurrent();
        metricInfo.put("time_to", getLastSendingTime());
        System.out.println("METRIC_SEND");
       /* for (var metric : metricInfo.entrySet()) {
            System.out.print(metric.getKey());
            System.out.print(": ");
            System.out.println(metric.getValue());
        } */
        byte[] out = Serializer.convertMetricInfo(
                metricInfo,
                token
        );
        try {
            HttpURLConnection http = createHttpURLConnection(URLs.POST_URL);
            return sendData(http, out);
        }
        catch (IOException e) {
            return false;
        }
    }

    public String submitUserInfo(UserInfo userInfo) throws WeNeedNameException {
        byte[] out = Serializer.convertUserInfo(userInfo);
        try {
            HttpURLConnection http = createHttpURLConnection(URLs.LOGIN_URL);
            if (!sendData(http, out)) {
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
            HttpURLConnection http = createHttpURLConnection(URLs.PLUGIN_GET_METRICS_URL);
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

    private HttpURLConnection createHttpURLConnection(URL url) throws IOException {
        URLConnection con = url.openConnection();
        return (HttpURLConnection) con;
    }

    private void updateSendingTimeToCurrent() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
        Date date = new Date(System.currentTimeMillis());
        lastSendingTime = formatter.format(date);
    }

    public String getLastSendingTime() {
        return lastSendingTime;
    }
}
