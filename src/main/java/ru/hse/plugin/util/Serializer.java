package ru.hse.plugin.util;

import org.json.simple.JSONValue;
import ru.hse.plugin.storage.UserInfo;
import ru.hse.plugin.storage.UserInfoHolder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Serializer {

    public static byte[] convertUserInfo(UserInfo userInfo) {
        Map<String, String> userInfoMap = new HashMap<>();
        String login = userInfo.getLoginNoExcept();
        String password = userInfo.getPasswordNoExcept();
        userInfoMap.put("username", login);
        userInfoMap.put("password", password);
        return JSONValue.toJSONString(userInfoMap).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] convertMetricInfo(Map<String, String> nameToMetric, String token) {
        nameToMetric.put("token", token);
        return JSONValue.toJSONString(nameToMetric).getBytes(StandardCharsets.UTF_8);
    }
}
