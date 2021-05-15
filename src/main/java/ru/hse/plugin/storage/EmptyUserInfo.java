package ru.hse.plugin.storage;

import ru.hse.plugin.util.WeNeedNameException;

public class EmptyUserInfo implements UserInfo {
    private boolean isLoginWindowNecessary;

    public EmptyUserInfo() {
        this(true);
    }

    public EmptyUserInfo(boolean isLoginWindowNecessary) {
        this.isLoginWindowNecessary = isLoginWindowNecessary;
    }

    @Override
    public String getLogin() throws WeNeedNameException {
        throw new WeNeedNameException("User info is empty");
    }

    @Override
    public String getPassword() throws WeNeedNameException {
        throw new WeNeedNameException("User info is empty");
    }

    @Override
    public boolean isSignedIn() {
        return false;
    }

    @Override
    public boolean isLoginWindowNecessary() {
        return isLoginWindowNecessary;
    }

    @Override
    public void setWindowNecessity(boolean necessity) {
        isLoginWindowNecessary = necessity;
    }

    @Override
    public String toString() {
        return isLoginWindowNecessary ? "true" : "false";
    }
}
