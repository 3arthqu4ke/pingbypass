package me.earth.pingbypass.api.config;

import com.google.gson.JsonElement;

public interface GsonSerializable extends JsonSerializable {
    @Override
    default JsonElement toJson() {
        return GSON.toJsonTree(this);
    }

}
