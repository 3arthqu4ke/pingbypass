package me.earth.pingbypass.api.config.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrentConfigManager implements ConfigFromFile {
    private Map<String, String> currentConfigs = new HashMap<>();

    public String getCurrentConfig(Nameable nameable) {
        return getCurrentConfig(nameable.getName());
    }

    public String getCurrentConfig(String name) {
        return currentConfigs.getOrDefault(name.toLowerCase(), "default");
    }

    public void setCurrentConfig(Nameable nameable, String currentConfig) {
        setCurrentConfig(nameable.getName(), currentConfig);
    }

    public void setCurrentConfig(String name, String currentConfig) {
        currentConfigs.put(name.toLowerCase(), currentConfig.toLowerCase());
    }

    @Override
    public void fromJson(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        this.currentConfigs = object
                .entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().getAsString()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        currentConfigs.forEach((key, value) -> object.add(key, new JsonPrimitive(value)));
        return object;
    }

}
