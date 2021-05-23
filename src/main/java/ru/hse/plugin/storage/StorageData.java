package ru.hse.plugin.storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.converters.JsonSenderConverter;
import ru.hse.plugin.converters.ListMetricConverter;
import ru.hse.plugin.converters.UserInfoConverter;
import ru.hse.plugin.metrics.AllCharCounter;
import ru.hse.plugin.metrics.Metric;
import ru.hse.plugin.util.PluginConstants;
import ru.hse.plugin.util.Serializer;

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

    // TODO mb do private, but nado chitat' kak serializovat'
    @OptionTag(converter = ListMetricConverter.class)
    @NotNull public List<Metric> metrics = List.of(new AllCharCounter());

    /*
        TODO я чуть-чуть хочу поменять логику и уже начал это делать
             теперь мы храним диффы метрик и отправляем их (как и раньше)
             но теперь мы храним накопленную метрику:
                 clear diff-ов - добавляем в accumulated
                 поддерживаем что diff[i] ~ accumulated[i]
                    Видимо надо будет хранить сет отдельно private полем (ну и хуй с ним)
                 при включение проекта пытаемся подсосать accumulated с сервера,
                    явно не отправляем никогда, потому что набираем просто диффами
                 мб что-то еще, хз
     */

    @OptionTag(converter = ListMetricConverter.class)
    @NotNull public List<Metric> accumulated = List.of(new AllCharCounter()); // TODO rename metrics to diff

    @OptionTag(converter = UserInfoConverter.class)
    @NotNull public UserInfo userInfo = new EmptyUserInfo();

    @OptionTag(converter = JsonSenderConverter.class)
    @NotNull public JsonSender jsonSender;

    public boolean doNotCollectAndSendInformation = false; // TODO support it!

    {
        try {
            jsonSender = new JsonSender(new URL("http://127.0.0.1:8000/post/"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Thread daemon = new Thread(() -> {
        try {
            while (!Thread.interrupted()) {
                System.out.println("                hello from daemon!");
                for (var metric : StorageData.getInstance().metrics) {
                    System.out.println("                    " + metric);
                }
                var instance = StorageData.getInstance();
                byte[] data = Serializer.convertMetricInfo(
                        instance.getMetricsInfo(),
                        instance.userInfo.getTokenNoExcept()
                );
                if (instance.jsonSender.sendData(data)) {
                    instance.clearMetrics();
                }
                // ------------------------------------------------------


                TimeUnit.SECONDS.sleep(PluginConstants.DAEMON_SLEEP_SECONDS);
            }
        } catch (InterruptedException ignored) { }
    });

    static {
        daemon.setDaemon(true); //TODO Но что эт значит...
    }

    public StorageData() {}

    public static synchronized StorageData getInstance() { // mb not synchronized, no hz...
        var instance = ServiceManager.getService(StorageData.class);
        instance.tryStartDaemon();
        return instance;
    }

    private void tryStartDaemon() {
        if (daemon.getState() == Thread.State.NEW) {
            daemon.start();
        }
    }

    @NotNull
    public Map<String, String> getMetricsInfo() {
        return metrics.stream().collect(Collectors.toMap(Metric::getName, Metric::getInfo));
    }

    public void clearMetrics() {
        metrics.forEach(Metric::clear); // TODO чистить -> чистить + добавлять в accumulated
    }

    public boolean setUserInfo(UserInfoHolder userInfo) {
        this.userInfo = userInfo;
        // TODO валидация и что-нибудь еще от сервера
        return true;
    }

    public void setUserInfo(EmptyUserInfo emptyUserInfo) {
        // mb send to server that you are signed out but mb not needed
        userInfo = emptyUserInfo;
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
