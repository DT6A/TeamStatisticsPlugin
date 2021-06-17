package ru.hse.plugin.storage;

import com.intellij.completion.ngram.slp.util.Pair;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.common.MetricSameHashSet;
import ru.hse.plugin.converters.JsonSenderConverter;
import ru.hse.plugin.converters.ListMetricConverter;
import ru.hse.plugin.converters.UserInfoConverter;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.typed.AllCharCounter;
import ru.hse.plugin.networking.JsonSender;
import ru.hse.plugin.networking.Sender;
import ru.hse.plugin.util.Constants;
import ru.hse.plugin.util.Util;
import ru.hse.plugin.util.WeNeedNameException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("CanBeFinal")
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
    @NotNull public List<Metric> diffs = new ArrayList<>();

    /*
        TODO я чуть-чуть хочу поменять логику и уже начал это делать
             теперь мы храним диффы метрик и отправляем их (как и раньше)
             но теперь мы храним накопленную метрику:
                 clear diff-ов - добавляем в accumulated
                 поддерживаем что diff[i] ~ accumulated[i] ( => пороядок важен)
                    Видимо надо будет хранить сет отдельно private полем (ну и хуй с ним)
                 при включение проекта пытаемся подсосать accumulated с сервера,
                    явно не отправляем никогда, потому что набираем просто диффами
                 мб что-то еще, хз
     */

    @OptionTag(converter = ListMetricConverter.class)
    @NotNull public List<Metric> accumulated = new ArrayList<>();

    @OptionTag(converter = UserInfoConverter.class)
    @NotNull public UserInfo userInfo = new EmptyUserInfo();

    @OptionTag(converter = JsonSenderConverter.class)
    @NotNull public JsonSender jsonSender = new JsonSender();

    private final Set<Metric> metrics = new MetricSameHashSet();

    public Set<Metric> metrics() {
        return metrics;
    }

    {
        if (diffs.isEmpty()) {
            diffs.add(new AllCharCounter());
            accumulated.add(new AllCharCounter());
        }
        metrics.addAll(diffs);
    }

    public boolean doNotCollectAndSendInformation = false; // TODO support it!

    private static final Thread daemon = new Thread(() -> {
        try {
            while (!Thread.interrupted()) {
              /*  System.out.println("                hello from daemon!");
                for (var metric : StorageData.getInstance().diffs) {
                    System.out.println("                    " + metric);
                } */
                var instance = StorageData.getInstance();
               // System.out.print("Token: ");
                //System.out.println(instance.userInfo.getTokenNoExcept());
                if (instance.userInfo.getTokenNoExcept() != null) {
                    System.out.println("Sending new metrics");
                    for (var kvp : instance.getMetricsInfo().entrySet()) {
                        System.out.println(kvp.getKey() + ": " + kvp.getValue());
                    }
                    Sender.sendMetrics();

                    Sender.updateMetrics();
                    // ------------------------------------------------------
                }
                TimeUnit.SECONDS.sleep(Constants.DAEMON_SLEEP_SECONDS);
            }
        } catch (InterruptedException ignored) { }
    });

    static {
        daemon.setDaemon(true);
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
        return diffs.stream().collect(Collectors.toMap(Metric::getName, Metric::getInfo));
    }

    public void clearMetrics() {
        System.out.println(diffs);
        System.out.println(accumulated);
        Util.zipWith(
                accumulated.stream(),
                diffs.stream(),
                Pair::of
        ).forEach(pair -> pair.left().mergeAndClear(pair.right()));
    }

    public boolean setUserInfo(UserInfoHolderBuilder userInfoBuilder)  {

        // TODO валидация
        try {
            var instance = StorageData.getInstance();
            String token = instance.jsonSender.submitUserInfo(
                    userInfoBuilder
            );
            this.userInfo = userInfoBuilder.create(token);
        } catch (WeNeedNameException e) {
            return false;
        }
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
        metrics.addAll(diffs);
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
        return Objects.equals(diffs, that.diffs) && Objects.equals(userInfo, that.userInfo)
                && Objects.equals(jsonSender, that.jsonSender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diffs, userInfo, jsonSender);
    }
}
