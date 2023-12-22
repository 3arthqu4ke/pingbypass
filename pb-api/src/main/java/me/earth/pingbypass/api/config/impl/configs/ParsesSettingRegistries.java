package me.earth.pingbypass.api.config.impl.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public interface ParsesSettingRegistries {
    Logger LOGGER = LoggerFactory.getLogger(ParsesSettingRegistries.class);

    default void fromJson(SettingRegistry container, JsonElement configuration) throws ConfigParseException {
        JsonObject containerObject = configuration.getAsJsonObject();
        for (var entry : containerObject.entrySet()) {
            Optional<Setting<?>> setting = container.getByName(entry.getKey());
            if (setting.isPresent()) {
                try {
                    setting.get().fromJson(entry.getValue());
                } catch (Exception e) {
                    LOGGER.error("Failed to parse setting " + container + " - " + setting.get().getName(), e);
                }
            }
        }
    }

}
