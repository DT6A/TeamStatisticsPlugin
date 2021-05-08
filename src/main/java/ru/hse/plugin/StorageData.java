package ru.hse.plugin;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.components.Storage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@State(
        name = "org.intellij.sdk.settings.StorageData",
        storages = {@Storage("StorageData.xml")}
)
public final class StorageData implements PersistentStateComponent<StorageData> { // TODO rename to Storage

    /*
     * как добавляются методы:
     * они все статик, а если надо пользоваться полями (например полем metrics, то пишем так: getInstance().metrics
     * на этом вроде бы всё...
     */

    @OptionTag(converter = ListMetricConverter.class)
    public List<Metric> metrics = List.of(new WordCounter("Cock"), new WordCounter("coq"));
    @OptionTag(converter = UserInfoConverter.class)
    public UserInfo userInfo = new UserInfo("Login", "pa$$word", 0);
    @OptionTag(converter = JsonSenderConverter.class)
    public JsonSender jsonSender;

    {
        try {
            jsonSender = new JsonSender(new URL("https://www.example.com/docs/resource1.html"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Thread daemon = new Thread(() -> {
        try {
            while (!Thread.interrupted()) {
//                var instance = StorageData.getInstance();
//                if (instance.jsonSender.sendData(instance.getMetricsInfo())) {
//                    instance.clearMetrics();
//                }
                // ------------------------------------------------------
                System.out.println("                hello from daemon!");
                for (var metric : StorageData.getInstance().metrics) {
                    System.out.println("                    " + metric);
                }

                TimeUnit.SECONDS.sleep(PluginConstants.DAEMON_SLEEP_SECONDS);
            }
        } catch (InterruptedException e) { }
    });

    static {
        daemon.setDaemon(true); // Но что эт значит...
    }

    public StorageData() {}

    public static synchronized StorageData getInstance() { // mb not synchronized, no hz...
        if (daemon.getState() == Thread.State.NEW) {
            daemon.start();
        }
        return ServiceManager.getService(StorageData.class);
    }

    @NotNull
    public Map<String, String> getMetricsInfo() {
        return metrics.stream().collect(Collectors.toMap(Metric::getName, Metric::getInfo));
    }

    public void clearMetrics() {
        metrics.forEach(Metric::clear);
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StorageData that = (StorageData) o;
        return Objects.equals(metrics, that.metrics) && Objects.equals(userInfo, that.userInfo)
                && Objects.equals(jsonSender, that.jsonSender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metrics, userInfo, jsonSender);
    }
}
