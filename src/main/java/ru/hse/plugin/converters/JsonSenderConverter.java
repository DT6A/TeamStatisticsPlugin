package ru.hse.plugin.converters;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.networking.JsonSender;

public class JsonSenderConverter extends Converter<JsonSender> {
    @Override
    public @Nullable JsonSender fromString(@NotNull String value) {
        return new JsonSender(value);
    }

    @Override
    public @Nullable String toString(@NotNull JsonSender value) {
        return value.getLastSendingTime();
    }
}
