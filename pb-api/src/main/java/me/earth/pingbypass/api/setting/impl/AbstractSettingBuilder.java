package me.earth.pingbypass.api.setting.impl;

import com.mojang.brigadier.arguments.ArgumentType;
import lombok.AccessLevel;
import lombok.Getter;
import me.earth.pingbypass.api.command.impl.util.ComponentUtil;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.config.impl.ConfigTypes;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;
import me.earth.pingbypass.api.traits.CanBeVisible;
import me.earth.pingbypass.api.traits.SelfTyped;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractSettingBuilder<T, S extends Setting<T>, B extends AbstractSettingBuilder<T, S, B>>
        implements SelfTyped<B> {
    private final List<SettingRegistry> registerOnBuild = new ArrayList<>(1);
    private Function<T, Component> componentFactory = ComponentUtil::getSimpleValueComponent;
    private CanBeVisible visibility = CanBeVisible.ALWAYS_VISIBLE;
    private ArgumentType<T> argumentType;
    private Complexity complexity = Complexities.BEGINNER;
    private JsonParser<T> parser;
    private ConfigType configType = ConfigTypes.SETTINGS;
    private String description;
    private T defaultValue;
    private String name;
    private T value;

    protected abstract S create();

    public B withName(String name) {
        this.name = name;
        return getSelf();
    }

    public B withComponentFactory(Function<T, Component> componentFactory) {
        this.componentFactory = componentFactory;
        return getSelf();
    }

    public B withConfigType(ConfigType configType) {
        this.configType = configType;
        return getSelf();
    }

    public B registerOnBuild(SettingRegistry container) {
        registerOnBuild.add(container);
        return getSelf();
    }

    public B withArgumentType(ArgumentType<T> argumentType) {
        this.argumentType = argumentType;
        return getSelf();
    }

    public B withVisibility(CanBeVisible visibility) {
        this.visibility = visibility;
        return getSelf();
    }

    public B withParser(JsonParser<T> parser) {
        this.parser = parser;
        return getSelf();
    }

    public B withComplexity(Complexity complexity) {
        this.complexity = complexity;
        return getSelf();
    }

    public B withDescription(String description) {
        this.description = description;
        return getSelf();
    }

    public B withValue(T value) {
        this.defaultValue = value;
        this.value = value;
        return getSelf();
    }

    public final B verify() {
        Objects.requireNonNull(this.getDefaultValue(), "Default value was null!");
        Objects.requireNonNull(this.getComponentFactory(), "ComponentFactory was null!");
        Objects.requireNonNull(this.getArgumentType(), "ArgumentType was null!");
        Objects.requireNonNull(this.getName(), "Name was null!");
        Objects.requireNonNull(this.getParser(), "Parser was null!");
        Objects.requireNonNull(this.getDescription(), "Description was null!");
        Objects.requireNonNull(this.getComplexity(), "Complexity was null!");
        Objects.requireNonNull(this.getConfigType(), "Config type was null!");
        return getSelf();
    }

    public final S build() {
        S setting = verify().create();
        registerOnBuild.forEach(container -> container.register(setting));
        return setting;
    }

    public S register(SettingRegistry container) {
        S setting = this.build();
        container.register(setting);
        return setting;
    }

}
