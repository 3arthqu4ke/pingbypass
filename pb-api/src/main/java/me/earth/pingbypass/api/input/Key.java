package me.earth.pingbypass.api.input;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.earth.pingbypass.api.config.GsonSerializable;
import me.earth.pingbypass.api.traits.Nameable;

@Data
public class Key implements Nameable, GsonSerializable {
    // TODO: update name if new keyboard...
    @EqualsAndHashCode.Exclude
    private final @SerializedName("name") String name;
    private final @SerializedName("type") Type type;
    private final @SerializedName("code") int code;

    public static Key fromJson(JsonElement element) {
        return GSON.fromJson(element, Key.class);
    }

    public enum Type {
        // GAMEPAD?
        KEYBOARD,
        MOUSE
    }

}
