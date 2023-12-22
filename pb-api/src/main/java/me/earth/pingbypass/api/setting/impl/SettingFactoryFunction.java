package me.earth.pingbypass.api.setting.impl;

import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.traits.CanBeVisible;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

@FunctionalInterface
public interface SettingFactoryFunction<T, S extends Setting<T>> {
    S create(Function<T, Component> componentFactory, CanBeVisible visibility, ArgumentType<T> argumentType, Complexity complexity,
             JsonParser<T> parser, ConfigType configType, String description, T defaultValue, String name);

}
