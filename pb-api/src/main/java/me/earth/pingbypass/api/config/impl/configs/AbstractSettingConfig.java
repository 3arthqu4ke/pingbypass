package me.earth.pingbypass.api.config.impl.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.impl.AbstractJsonConfig;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.SettingRegistry;
import me.earth.pingbypass.api.traits.Nameable;

@Slf4j
public abstract class AbstractSettingConfig<T extends SettingRegistry & Nameable> extends AbstractJsonConfig
        implements ParsesSettingRegistries {
    private final Iterable<T> containers;

    public AbstractSettingConfig(FileManager fileManager, ConfigType configType, Iterable<T> containers) {
        super(fileManager, configType);
        this.containers = containers;
    }

    protected boolean shouldConfigure(Setting<?> setting) {
        return setting.getConfigType().equals(this.getConfigType());
    }

    @Override
    public void fromJson(JsonElement element) throws ConfigParseException {
        JsonObject object = element.getAsJsonObject();
        for (T t : containers) {
            JsonElement configuration = object.get(t.getName());
            if (configuration == null) {
                log.warn("Could not find configuration for %s in the %s config!".formatted(t.getName(), getName()));
                continue;
            }

            fromJson(t, configuration);
        }
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        for (T container : containers) {
            JsonElement containerObject = toJson(container);
            object.add(container.getName(), containerObject);
        }

        return object;
    }

    public JsonElement toJson(SettingRegistry container) {
        JsonObject object = new JsonObject();
        for (Setting<?> setting : container) {
            if (shouldConfigure(setting)) {
                object.add(setting.getName(), setting.toJson());
            }
        }

        return object;
    }

}
