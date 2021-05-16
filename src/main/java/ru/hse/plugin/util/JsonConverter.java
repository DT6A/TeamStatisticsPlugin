package ru.hse.plugin.util;

import org.json.simple.JSONValue;
import ru.hse.plugin.storage.UserInfo;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonConverter {

    public static byte[] convertUserInfo(UserInfo userInfo) {
        return null;
    }

    public static byte[] convertMetricInfo(Map<String, String> nameToMetric) {
        // TODO get token
        nameToMetric.put("user_id", "1");
        return JSONValue.toJSONString(nameToMetric).getBytes(StandardCharsets.UTF_8);
    }
}
