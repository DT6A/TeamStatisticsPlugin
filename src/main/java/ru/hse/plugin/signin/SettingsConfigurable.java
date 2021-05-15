package ru.hse.plugin.signin;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.storage.StorageData;

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
