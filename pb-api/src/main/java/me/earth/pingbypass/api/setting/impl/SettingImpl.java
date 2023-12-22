package me.earth.pingbypass.api.setting.impl;

import com.google.gson.JsonElement;
import com.mojang.brigadier.arguments.ArgumentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingEvent;
import me.earth.pingbypass.api.traits.CanBeVisible;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

@Getter
@Setter
public class SettingImpl<T> implements Setting<T> {
    @Delegate
    private final SettingObserverService<T> observerService = new SettingObserverService<>();
    private final Function<T, Component> componentFactory;
    @Delegate(types = CanBeVisible.class)
    private final CanBeVisible visibility;
    private final ArgumentType<T> argumentType;
    private final Complexity complexity;
    @Getter(value = AccessLevel.PRIVATE)
    private final JsonParser<T> parser;
    private final ConfigType configType;
    private final String description;
    private final T defaultValue;
    private final String name;
    private T value;

    public boolean setValue(T value) {
        SettingEvent<T> event = observerService.notifyPre(this, value);
        if (!event.isCancelled()) {
            this.value = value;
            observerService.notifyPost(event);
            return true;
        }

        return false;
    }

    public SettingImpl(Function<T, Component> componentFactory, CanBeVisible visibility, ArgumentType<T> argumentType, Complexity complexity,
                       JsonParser<T> parser, ConfigType configType, String description, T defaultValue, String name) {
        this.componentFactory = componentFactory;
        this.visibility = visibility;
        this.argumentType = argumentType;
        this.complexity = complexity;
        this.parser = parser;
        this.configType = configType;
        this.description = description;
        this.defaultValue = defaultValue;
        this.name = name;
        this.value = defaultValue;
    }

    @Override
    public void fromJson(JsonElement element) {
        this.setValue(parser.deserialize(element));
    }

    @Override
    public JsonElement toJson() {
        return parser.serialize(this.getValue());
    }

    @Override
    public Component getValueComponent() {
        return componentFactory.apply(getValue());
    }

}
