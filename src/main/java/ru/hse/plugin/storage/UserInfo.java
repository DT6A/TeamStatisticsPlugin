package ru.hse.plugin.storage;

import ru.hse.plugin.util.WeNeedNameException;

public interface UserInfo {
    String getLogin() throws WeNeedNameException;
    String getPassword() throws WeNeedNameException;
    boolean isSignedIn();
    boolean isLoginWindowNecessary();
    void setWindowNecessity(boolean necessity);
}
