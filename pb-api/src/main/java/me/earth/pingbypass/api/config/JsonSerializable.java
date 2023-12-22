package me.earth.pingbypass.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Interface for Objects that can be converted to a {@link JsonElement}.
 */
public interface JsonSerializable {
    String _JSON = ".json";

    Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .setExclusionStrategies(new ExcludeNonAnnotatedFieldsStrategy())
        .create();

    /**
     * @return the value of this object as a {@link JsonElement}.
     */
    JsonElement toJson();

}
