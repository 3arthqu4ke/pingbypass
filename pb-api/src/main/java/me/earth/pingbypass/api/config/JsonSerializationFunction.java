package me.earth.pingbypass.api.config;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface JsonSerializationFunction<T> {
    JsonElement serialize(T element);

}
