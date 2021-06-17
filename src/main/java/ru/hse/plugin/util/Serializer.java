package ru.hse.plugin.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.commons.util.Names;
import ru.hse.plugin.metrics.copypaste.SpecificLengthCopyCounter;
import ru.hse.plugin.metrics.copypaste.SpecificLengthPasteCounter;
import ru.hse.plugin.metrics.git.SpecificBranchCommitCounter;
import ru.hse.plugin.metrics.typed.CharCounter;
import ru.hse.plugin.metrics.typed.SubstringCounter;
import ru.hse.plugin.metrics.typed.WordCounter;
import ru.hse.plugin.storage.UserInfo;
import ru.hse.plugin.storage.UserInfoHolder;
import ru.hse.plugin.storage.UserInfoHolderBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

    public static byte[] convertToken(String token) {
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return JSONValue.toJSONString(data).getBytes(StandardCharsets.UTF_8);
    }

    public static Set<Metric> addMetricFromJson(JSONObject obj) {
        Set<Metric> metrics = new HashSet<>();
        addNonParametrizedMetrics(obj, metrics);
       // System.out.println(metrics.size());
        JSONArray charCounting = obj.optJSONArray(Names.CHAR_COUNTER);
        if (charCounting != null) {
            for (int i = 0; i < charCounting.length(); i++) {
                char character = charCounting.getString(i).charAt(0);
                CharCounter charCounter = new CharCounter(character);
                metrics.add(charCounter);
            }
        }
        addStringParametrizedMetric(obj, metrics,
                Names.WORD_COUNTER, WordCounter::new);
        addStringParametrizedMetric(obj, metrics,
                Names.SUBSTRING_COUNTER, SubstringCounter::new);
        addStringParametrizedMetric(obj, metrics,
                Names.SPECIFIC_BRANCH_COMMIT_COUNTER, SpecificBranchCommitCounter::new);
        addIntegerParametrizedMetric(obj, metrics,
                Names.SPECIFIC_LENGTH_COPY_COUNTER, SpecificLengthCopyCounter::new);
        addIntegerParametrizedMetric(obj, metrics,
                Names.SPECIFIC_LENGTH_PASTE_COUNTER, SpecificLengthPasteCounter::new);
        return metrics;
    }

    private static void addNonParametrizedMetrics(JSONObject obj, Set<Metric> metrics) {
        for (var entry : Names.NON_PARAMETRIZED_METRICS_CONSTRUCTORS.entrySet()) {
            if (!obj.optString(entry.getKey()).isEmpty()) {
                metrics.add(entry.getValue().get());
            }
        }
    }

    private static void addStringParametrizedMetric(JSONObject obj,
                                                    Set<Metric> metrics,
                                                    String name,
                                                    Function<String, Metric> constructor) {
        JSONArray metricsWithStringParam = obj.getJSONArray(name);
        if (metricsWithStringParam != null) {
            for (int i = 0; i < metricsWithStringParam.length(); i++) {
                String param = metricsWithStringParam.getString(i);
                metrics.add(constructor.apply(param));
            }
        }
    }

    private static void addIntegerParametrizedMetric(JSONObject obj,
                                                     Set<Metric> metrics,
                                                     String name,
                                                     Function<Integer, Metric> constructor) {
        JSONArray metricsWithIntegerParam = obj.getJSONArray(name);
        if (metricsWithIntegerParam != null) {
            for (int i = 0; i < metricsWithIntegerParam.length(); i++) {
                int param = metricsWithIntegerParam.getInt(i);
                metrics.add(constructor.apply(param));
            }
        }
    }
}
