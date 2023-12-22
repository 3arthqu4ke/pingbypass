package me.earth.pingbypass.api.setting.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.config.Configurable;
import me.earth.pingbypass.api.config.impl.configs.ParsesSettingRegistries;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ConfigurableSettingRegistry extends Configurable, SettingRegistry {
    Logger LOGGER = LoggerFactory.getLogger(ParsesSettingRegistries.class);

    @Override
    default void fromJson(JsonElement element) throws ConfigParseException {
        if (!element.isJsonObject()) {
            throw new ConfigParseException("%s is not a JsonObject!".formatted(element));
        }

        this.fromJsonObject(element.getAsJsonObject());
    }

    @Override
    default JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (Setting<?> setting : this) {
            JsonElement settingElement = setting.toJson();
            object.add(setting.getName(), settingElement);
        }

        return object;
    }

    default void fromJsonObject(JsonObject object) throws ConfigParseException {
        for (Setting<?> setting : this) {
            JsonElement settingElement = object.get(setting.getName());
            if (settingElement != null) {
                try {
                    setting.fromJson(settingElement);
                } catch (Exception e) {
                    LOGGER.error("Failed to parse setting " + this + " - " + setting.getName(), e);
                }
            }
        }
    }

}
