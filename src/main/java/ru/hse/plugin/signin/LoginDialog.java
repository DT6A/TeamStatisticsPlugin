package ru.hse.plugin.signin;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.storage.UserInfoHolder;
import ru.hse.plugin.util.Constants;
import ru.hse.plugin.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginDialog extends DialogWrapper implements ActionListener {
    private final String message;
    private final JTextField login = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JButton signUpButton = new JButton("Sign up");
    private final JCheckBox doNotAskAgain = new JCheckBox("Do not ask again");

    {
        signUpButton.addActionListener(this);
    }

    public final JPanel centralPanel = new JPanel(new GridBagLayout());

    public LoginDialog(@Nullable String message) {
        super(true);
        this.message = message;
        setTitle("Nash Plugin");
        init();
    }

    public LoginDialog() {
        this(null);
    }

    @Override
    @Nullable
    protected JComponent createCenterPanel() {
        final var gridBag = new GridBag()
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL)
                .setDefaultInsets(JBUI.insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP));

        if (message != null) {
            var constraint = gridBag.nextLine().next();
            constraint.gridwidth = 2;
            constraint.fill = GridBagConstraints.CENTER;
            centralPanel.add(getLabel(message), constraint);
        }

        centralPanel.setPreferredSize(new Dimension(300, 50));

        centralPanel.add(getLabel("Login: "), gridBag.nextLine().next().weightx(0.2));
        centralPanel.add(login, gridBag.next().weightx(0.8));

        centralPanel.add(getLabel("Password: "), gridBag.nextLine().next().weightx(0.2));
        centralPanel.add(password, gridBag.next().weightx(0.8));

        centralPanel.add(signUpButton, gridBag.nextLine().next().weightx(0.2));

        var constraint = gridBag.nextLine().next();
        constraint.gridwidth = 2;
        centralPanel.add(doNotAskAgain, constraint);

        return centralPanel;
    }

    @Override
    protected void doOKAction() {
        System.out.println("Login: " + login.getText() + ", Password: " + new String(password.getPassword()));
        super.doOKAction();
    }

    private JComponent getLabel(String text) {
        final var label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.LARGE);
        label.setFontColor(UIUtil.FontColor.NORMAL);
        label.setBorder(JBUI.Borders.empty(5));
        return label;
    }

    // TODO вот тут странно отдается пустой token, это заглушка, надо бы тут что-то с сервером делать уже
    public UserInfoHolder getUserInfo() {
        return new UserInfoHolder(login.getText(), password.getPassword(), "token");
    }

    public boolean doNotAskAgain() {
        return doNotAskAgain.isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpButton) {
            Util.openLink(Constants.REGISTRATION_LINK);
        }
    }
}
