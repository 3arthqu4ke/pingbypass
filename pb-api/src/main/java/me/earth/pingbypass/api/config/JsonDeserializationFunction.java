package me.earth.pingbypass.api.config;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface JsonDeserializationFunction<T> {
    T deserialize(JsonElement element);

}
