package ru.hse.plugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SettingsConfigurable implements Configurable {
    SettingsComponent settingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Nikita Loh"; // TODO
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsComponent = new SettingsComponent();
        return new SettingsComponent().getPanel();
    }

    @Override
    public boolean isModified() {
        return StorageData.getInstance().doNotCollectAndSendInformation
                != settingsComponent.doNotCollectAndSendInformation();
    }

    @Override
    public void apply() {
        StorageData.getInstance().doNotCollectAndSendInformation = settingsComponent.doNotCollectAndSendInformation();
    }
}
