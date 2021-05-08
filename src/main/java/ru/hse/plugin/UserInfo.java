package ru.hse.plugin;

import java.util.Objects;

public class UserInfo {
    private final String login;
    private final String password;
    private final long id;

    public UserInfo(String login, String password, long id) {
        if (login.matches(".*[ \t\n].*") || password.matches(".*[ \t\n].*")) {
            throw new RuntimeException("Illegal symbols in login or password");
            // TODO как-нибудь разумнее искать совпадение 1 символа + разделить (видимо)
            //      на разные случаие эксепшн (НО хочется чтобы это тупо на сервере проверялось)
        }

        this.login = login;
        this.password = password;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return id == userInfo.id && Objects.equals(login, userInfo.login)
                && Objects.equals(password, userInfo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, id);
    }
}
