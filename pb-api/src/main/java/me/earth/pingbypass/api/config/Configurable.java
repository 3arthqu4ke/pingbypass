package me.earth.pingbypass.api.config;

import com.google.gson.JsonElement;

/**
 * Interface for Objects that can be serialized using Json.
 */
public interface Configurable extends JsonSerializable {
    /**
     * Sets values of this object from the given {@link JsonElement}.
     *
     * @param element the element.
     */
    void fromJson(JsonElement element) throws ConfigParseException;

}
