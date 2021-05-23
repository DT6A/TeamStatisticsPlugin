package ru.hse.plugin.signin;

import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import ru.hse.plugin.util.PluginConstants;
import ru.hse.plugin.util.Util;
import ru.hse.plugin.storage.EmptyUserInfo;
import ru.hse.plugin.storage.StorageData;
import ru.hse.plugin.storage.UserInfoHolder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SettingsComponent implements ActionListener {
    private final JPanel settingPanel;

    private final JCheckBox doNotCollectAndSendInformation =
            new JCheckBox("Do not collect and send any information");

    private final JLabel notSignedInInfo = getLabel(
            "You are not currently signed in in plugin, so please sign in: "
    );

    private final JLabel loginLabel = getLabel("Login: ");
    private final JBTextField loginField = new JBTextField();

    private final JLabel passwordLabel = getLabel("Password: ");
    private final JBPasswordField passwordField = new JBPasswordField();

    private final JButton signInButton = new JButton("Sign in");

    private final JButton signUpButton = new JButton("Sign up");

    private final JLabel userInfoLabel = getLabel("TEXT NOT SET YET");
    private final JButton signOutButton = new JButton("Sign out");

    private final List<JComponent> notSignedInComponents = List.of(
            notSignedInInfo, loginLabel, loginField, passwordLabel, passwordField, signInButton, signUpButton
    );
    private final List<JComponent> signedInComponents = List.of(
            userInfoLabel, signOutButton
    );

    public SettingsComponent() {
        signInButton.addActionListener(this);
        signUpButton.addActionListener(this);
        signOutButton.addActionListener(this);

        settingPanel = makePanel();
        setVisibility();
    }

    public JPanel getPanel() {
        return settingPanel;
    }

    private JLabel getLabel(String text) {
        final var label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.LARGE);
        label.setFontColor(UIUtil.FontColor.NORMAL);
        label.setBorder(JBUI.Borders.empty(5));
        return label;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == signInButton) {
            if (StorageData.getInstance().setUserInfo(getNewUserInfo())) {
                setVisibility();
                loginField.setText("");
                passwordField.setText("");
            } else {
                Messages.showInfoMessage("Could not sign in", "Plugin Information");
            }
        } else if (event.getSource() == signUpButton) {
            Util.openLink(PluginConstants.REGISTRATION_LINK);
        } else if (event.getSource() == signOutButton) {
            StorageData.getInstance().setUserInfo(makeEmptyUserInfo());
            setVisibility();
        }
    }

    public boolean doNotCollectAndSendInformation() {
        return doNotCollectAndSendInformation.isSelected();
    }

    private EmptyUserInfo makeEmptyUserInfo() {
        return new EmptyUserInfo();
    }

    // TODO Аналогично LoginDialog.java:89
    private UserInfoHolder getNewUserInfo() {
        return new UserInfoHolder(loginField.getText(), passwordField.getPassword(), "");
    }

    private JPanel makePanel() {
        return FormBuilder.createFormBuilder()
                .addComponent(doNotCollectAndSendInformation)
                .addComponent(notSignedInInfo)
                .addLabeledComponent(loginLabel, loginField)
                .addLabeledComponent(passwordLabel, passwordField)
                .addComponent(signInButton)
                .addComponent(signUpButton)

                .addComponent(userInfoLabel)
                .addComponent(signOutButton)

                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    private void setVisibility() {
        if (StorageData.getInstance().userInfo.isSignedIn()) {
            notSignedInComponents.forEach(c -> c.setVisible(false));
            signedInComponents.forEach(c -> c.setVisible(true));

            UserInfoHolder userInfoHolder = (UserInfoHolder) StorageData.getInstance().userInfo;

            userInfoLabel.setText("You are currently logged in as " + userInfoHolder.getLogin());
        } else {
            notSignedInComponents.forEach(c -> c.setVisible(true));
            signedInComponents.forEach(c -> c.setVisible(false));
        }
    }
}
