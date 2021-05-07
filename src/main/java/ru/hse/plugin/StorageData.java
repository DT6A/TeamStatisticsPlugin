package ru.hse.plugin;

import com.intellij.openapi.components.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.xmlb.XmlSerializationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@State(
        name = "org.intellij.sdk.settings.StorageData",
        storages = {@Storage("StorageData.xml")}
)
public final class StorageData implements PersistentStateComponent<StorageData> { // TODO randme to Storage
    @OptionTag(converter = MetricConverter.class) // mb does not work..., mb not metric converter but list<metric> cnv
    public List<Metric> metrics = new ArrayList<>(); // mb Map<Metric name / Metric class --> Metric>
    @OptionTag(converter = UserInfoConverter.class)
    public UserInfo userInfo = new UserInfo("Login", "pa$$word", 0);
    @OptionTag(converter = JsonSenderConverter.class)
    public JsonSender jsonSender = new JsonSender(null);

    public StorageData() {}

    public static StorageData getInstance() { // mb synchronized, no hz...
        return ServiceManager.getService(StorageData.class);
    }

    @Override
    public @NotNull StorageData getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull StorageData state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageData that = (StorageData) o;
        return Objects.equals(metrics, that.metrics) && Objects.equals(userInfo, that.userInfo)
                && Objects.equals(jsonSender, that.jsonSender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metrics, userInfo, jsonSender);
    }
}
