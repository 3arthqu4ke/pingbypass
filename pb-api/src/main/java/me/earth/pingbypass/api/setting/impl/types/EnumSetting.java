package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.impl.SettingImpl;
import me.earth.pingbypass.api.traits.CanBeVisible;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class EnumSetting<T extends Enum<T>> extends SettingImpl<T> {
    public EnumSetting(Function<T, Component> componentFactory, CanBeVisible visibility, ArgumentType<T> argumentType, Complexity complexity, JsonParser<T> parser,
                       ConfigType configType, String description, T defaultValue, String name) {
        super(componentFactory, visibility, argumentType, complexity, parser, configType, description, defaultValue, name);
    }

    @Override
    public Class<T> getType() {
        return getDefaultValue().getDeclaringClass();
    }

}
