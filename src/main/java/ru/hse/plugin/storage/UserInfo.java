package ru.hse.plugin.storage;

import ru.hse.plugin.util.WeNeedNameException;

public interface UserInfo {
    String getLogin() throws WeNeedNameException;
    String getPassword() throws WeNeedNameException;
    String getToken() throws WeNeedNameException;

    default String getLoginNoExcept() {
        String login = "";
        try {
            login = getLogin();
        } catch (WeNeedNameException ignored) {}
        return login;
    }

    default String getPasswordNoExcept() {
        String password = "";
        try {
            password = getPassword();
        } catch (WeNeedNameException ignored) {}
        return password;
    }

    default String getTokenNoExcept() {
        String token = "";
        try {
            token = getToken();
        } catch (WeNeedNameException ignored) {}
        return token;
    }
    boolean isSignedIn();
    boolean isLoginWindowNecessary();
    void setWindowNecessity(boolean necessity);
}
