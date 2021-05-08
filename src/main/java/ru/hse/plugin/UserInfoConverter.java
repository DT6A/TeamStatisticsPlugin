package ru.hse.plugin;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserInfoConverter extends Converter<UserInfo> {
    /*
     * assuming there is no spaces in login, password, id
     */
    @Override
    public @Nullable UserInfo fromString(@NotNull String value) {
        String[] fields = value.split(" ");
        if (fields.length != 3) {
            throw new RuntimeException("Parse error: expected 3 spaces, found " + (fields.length - 1));
        }
        return new UserInfo(fields[0], fields[1], Long.parseLong(fields[2]));
    }


    /*
     * TODO: пароли надо как-то не так хранить,
     *       https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html#store-credentials
     */
    @Override
    public @Nullable String toString(@NotNull UserInfo value) {
        return value.getLogin() + " " + value.getPassword() + " " + value.getId();
    }
}
