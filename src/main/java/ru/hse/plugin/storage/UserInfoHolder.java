package ru.hse.plugin.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.StringJoiner;

public class UserInfoHolder implements UserInfo {
    @NotNull private final String login;
    @NotNull private final String password;
    private String token;

    public UserInfoHolder(@NotNull String login, @NotNull String password, @NotNull String token) {
        if (login.matches(".*[ \t\n].*") || password.matches(".*[ \t\n].*")) {
            throw new RuntimeException("Illegal symbols in login or password");
            // TODO как-нибудь разумнее искать совпадение 1 символа + разделить (видимо)
            //      на разные случаие эксепшн (НО хочется чтобы это тупо на сервере проверялось)
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
    public void setToken(String token) {
        this.token = token;
    }

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

    // TODO
    //          Короче здесь пока token не создан может кидать что-нибудь,
    //      если сделаешь типа UserInfoHolderBuilder, то будет круто,
    //      чтобы UserInfoHolder всегда было в корректном состоянии
    //      и токен был бы @NotNull private final String token
    @Override
    public String toString() {
        return new StringJoiner(" ").add(login).add(password).add(token).toString();
    }
}
