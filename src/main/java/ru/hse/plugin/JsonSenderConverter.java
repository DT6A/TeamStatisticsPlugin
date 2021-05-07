package ru.hse.plugin;

import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URL;

public class JsonSenderConverter extends Converter<JsonSender> {
    @Override
    public @Nullable JsonSender fromString(@NotNull String value) {
        try {
            return new JsonSender(new URL(value));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable String toString(@NotNull JsonSender value) {
        return value.getUrl().toString();
    }
}
