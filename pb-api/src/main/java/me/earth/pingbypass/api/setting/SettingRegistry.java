package me.earth.pingbypass.api.setting;

import me.earth.pingbypass.api.registry.OrderedRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface SettingRegistry extends OrderedRegistry<Setting<?>> {
    @SuppressWarnings({"unchecked", "rawtypes"})
    default <T> Optional<Setting<T>> getSetting(String name, @Nullable Class<T> type) {
        Optional<Setting<?>> setting = getByName(name);
        if (type == null || setting.isPresent() && type.isAssignableFrom(setting.get().getType())) {
            return (Optional) setting;
        }

        return Optional.empty();
    }

    // most settings are SettingImpl anyways...
    default <T, S extends Setting<T>> Optional<S> getSettingImplementation(String name, Class<S> clazz) {
        return getSettingImplementation(name, clazz, null);
    }

    // most settings are SettingImpl anyways...
    default <T, S extends Setting<T>> Optional<S> getSettingImplementation(String name, Class<S> clazz, @Nullable Class<T> type) {
        Optional<S> setting = getByClass(name, clazz);
        if (type == null || setting.isPresent() && type.isAssignableFrom(setting.get().getType())) {
            return setting;
        }

        return Optional.empty();
    }

}
