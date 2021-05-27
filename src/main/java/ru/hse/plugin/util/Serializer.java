package ru.hse.plugin.util;

import org.json.simple.JSONValue;
import ru.hse.plugin.storage.UserInfo;
import ru.hse.plugin.storage.UserInfoHolder;
import ru.hse.plugin.storage.UserInfoHolderBuilder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
}
