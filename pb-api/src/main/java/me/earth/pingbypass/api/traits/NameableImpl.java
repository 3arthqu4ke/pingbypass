package me.earth.pingbypass.api.traits;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.ArgumentType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.JsonSerializable;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class NameableImpl implements Nameable, JsonSerializable {
    private final String name;

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public JsonElement toJson() {
        return nameableToJson(this);
    }

    public static NameableImpl fromJson(JsonElement element) {
        return new NameableImpl(element.isJsonObject() ? element.toString() : element.isJsonNull() ? "null" : element.getAsString());
    }

    public static JsonElement nameableToJson(Nameable nameable) {
        return new JsonPrimitive(nameable.getName());
    }

    public static ArgumentType<Nameable> simpleArgumentType() {
        return stringReader -> new NameableImpl(stringReader.readString());
    }

}
