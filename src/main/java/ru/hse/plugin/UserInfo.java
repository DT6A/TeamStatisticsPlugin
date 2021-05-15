package ru.hse.plugin;

public interface UserInfo {
    String getLogin() throws WeNeedNameException;
    String getPassword() throws WeNeedNameException;
    boolean isSignedIn();
    boolean isLoginWindowNecessary();
    void setWindowNecessity(boolean necessity);
}
