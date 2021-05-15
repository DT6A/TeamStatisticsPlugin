package ru.hse.plugin;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UserInfoHolder implements UserInfo {
    @NotNull private final String login;
    @NotNull private final String password;

    public UserInfoHolder(@NotNull String login, @NotNull String password) {
        if (login.matches(".*[ \t\n].*") || password.matches(".*[ \t\n].*")) {
            throw new RuntimeException("Illegal symbols in login or password");
            // TODO как-нибудь разумнее искать совпадение 1 символа + разделить (видимо)
            //      на разные случаие эксепшн (НО хочется чтобы это тупо на сервере проверялось)
        }

        this.login = login;
        this.password = password;
    }

    public UserInfoHolder(@NotNull String login, char[] password) {
        this(login, new String(password));
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoHolder that = (UserInfoHolder) o;
        return login.equals(that.login) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
