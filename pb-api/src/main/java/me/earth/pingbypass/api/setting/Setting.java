package me.earth.pingbypass.api.setting;

import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.Configurable;
import me.earth.pingbypass.api.traits.CanBeVisible;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.network.chat.Component;

public interface Setting<T> extends HoldsValue<T>, Configurable, Nameable,
        HasDescription, CanBeVisible, PreAndPostObservable<SettingObserver<T>> {
    T getDefaultValue();

    ArgumentType<T> getArgumentType();

    Complexity getComplexity();

    ConfigType getConfigType();

    Component getValueComponent();

    @SuppressWarnings("unchecked")
    default Class<T> getType() {
        return (Class<T>) getDefaultValue().getClass();
    }

}
