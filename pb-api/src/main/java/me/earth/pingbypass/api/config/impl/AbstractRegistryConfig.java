package me.earth.pingbypass.api.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.traits.Nameable;

@Slf4j
public abstract class AbstractRegistryConfig<N extends Nameable, R extends Registry<N>> extends AbstractJsonConfig {
    private final R registry;

    public AbstractRegistryConfig(FileManager fileManager, ConfigType configType, R registry) {
        super(fileManager, configType);
        this.registry = registry;
    }

    protected abstract JsonElement serialize(N element);

    protected abstract N deserialize(JsonElement element) throws JsonParseException;

    @Override
    public void fromJson(JsonElement element) throws ConfigParseException {
        registry.clear();
        for (JsonElement arrayElement : element.getAsJsonArray()) {
            try {
                N object = deserialize(arrayElement);
                registry.register(object);
            } catch (JsonParseException e) {
                log.error("Failed to deserialize element " + element, e);
            }
        }
    }

    @Override
    public JsonElement toJson() {
        JsonArray array = new JsonArray((int) registry.stream().count());
        registry.stream().map(this::serialize).forEach(array::add);
        return array;
    }

}
