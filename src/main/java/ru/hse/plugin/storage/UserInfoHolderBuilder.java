package ru.hse.plugin.storage;

public class UserInfoHolderBuilder {

    private final String username;
    private final String password;

    public UserInfoHolderBuilder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserInfo create(String token) {
        return new UserInfoHolder(username, password, token);
    }


}
