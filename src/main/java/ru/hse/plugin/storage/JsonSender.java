package ru.hse.plugin.storage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONException;
import ru.hse.plugin.util.WeNeedNameException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

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
                String token = null;
                try (BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(http.getInputStream()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        JSONObject obj = new JSONObject(line);
                        token = obj.getString("name");
                    }
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
