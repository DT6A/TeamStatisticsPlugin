package ru.hse.plugin;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserInfoConverter extends Converter<UserInfo> {
    /*
     * assuming there is no spaces in login, password, id
     */
    @Override
    public @Nullable UserInfo fromString(@NotNull String value) {
        if (value.equals("true")) {
            return new EmptyUserInfo(true);
        } else if (value.equals("false")) {
            return new EmptyUserInfo(false);
        }
        String[] fields = value.split(" ");
        if (fields.length != 2) {
            throw new RuntimeException("Parse error: expected 2 spaces or \"" +
                    PluginConstants.EMPTY_USER_INFO + "\", found " + (fields.length - 1));
        }
        return new UserInfoHolder(fields[0], fields[1]);
    }


    /*
     * TODO: пароли надо как-то не так хранить,
     *       https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html#store-credentials
     */
    @Nullable
    @Override
    public String toString(@NotNull UserInfo value) {
        return value.toString();
    }
}
