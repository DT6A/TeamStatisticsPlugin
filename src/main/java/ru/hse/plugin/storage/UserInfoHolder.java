package ru.hse.plugin.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.StringJoiner;

public class UserInfoHolder implements UserInfo {
    @NotNull private final String login;
    @NotNull private final String password;
    @NotNull private final String token;

    public UserInfoHolder(@NotNull String login, @NotNull String password, @NotNull String token) {
        if (login.matches(".*[ \t\n].*") || password.matches(".*[ \t\n].*")) {
            throw new RuntimeException("Illegal symbols in login or password");
        }
        this.login = login;
        this.password = password;
        this.token = token;
    }

    public UserInfoHolder(@NotNull String login, char[] password, @NotNull String token) {
        this(login, new String(password), token);
    }

    @NotNull
    @Override
    public String getLogin() {
        return login;
    }

    @NotNull
    @Override
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getToken() {
        return token;
    }

    @Override
    public boolean isSignedIn() {
        return true;
    }

    @Override
    public boolean isLoginWindowNecessary() {
        return false;
    }

    @Override
    public void setWindowNecessity(boolean necessity) { }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfoHolder that = (UserInfoHolder) o;
        return login.equals(that.login) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return new StringJoiner(" ").add(login).add(password).add(token).toString();
    }
}
