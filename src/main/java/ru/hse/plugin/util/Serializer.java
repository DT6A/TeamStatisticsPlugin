package ru.hse.plugin.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.util.Names;
import ru.hse.plugin.metrics.typed.CharCounter;
import ru.hse.plugin.metrics.typed.WordCounter;
import ru.hse.plugin.storage.UserInfo;
import ru.hse.plugin.storage.UserInfoHolder;
import ru.hse.plugin.storage.UserInfoHolderBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Serializer {

    public static byte[] convertUserInfoForSubmit(UserInfoHolderBuilder builder) {
        Map<String, String> userInfoMap = new HashMap<>();
        String username = builder.getUsername();
        String password = builder.getPassword();
        userInfoMap.put("username", username);
        userInfoMap.put("password", password);
        return JSONValue.toJSONString(userInfoMap).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] convertMetricInfo(Map<String, String> nameToMetric, String token) {
        nameToMetric.put("token", token);
        return JSONValue.toJSONString(nameToMetric).getBytes(StandardCharsets.UTF_8);
    }

    public static Set<Metric> addMetricFromJson(JSONObject obj) {
        Set<Metric> metrics = new HashSet<>();

        addNonParametrizedMetrics(obj, metrics);
        JSONArray charCounting = obj.getJSONArray(Names.CHAR_COUNTER);
        JSONArray wordCounting = obj.getJSONArray(Names.WORD_COUNTER);
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
        return metrics;
    }

    private static void addNonParametrizedMetrics(JSONObject obj, Set<Metric> metrics) {
        for (var entry : Names.NON_PARAMETRIZED_METRICS_CONSTRUCTORS.entrySet()) {
            if (obj.optString(entry.getKey(), null) != null) {
                metrics.add(entry.getValue().get());
            }
        }
    }
}
