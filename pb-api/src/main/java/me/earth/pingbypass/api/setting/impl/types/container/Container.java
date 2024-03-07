package me.earth.pingbypass.api.setting.impl.types.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.config.JsonSerializable;
import me.earth.pingbypass.api.config.JsonSerializationFunction;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.registry.impl.OrderedRegistryImpl;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.NameableImpl;
import me.earth.pingbypass.api.traits.Streamable;

import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

// TODO: mutability should not be exposed to the outside?
// TODO: make contains faster, also containsIgnoreCase
@Getter
@RequiredArgsConstructor
public class Container<T extends Nameable> extends OrderedRegistryImpl<T> implements Streamable<T>, JsonSerializable {
    private final @Nullable ContainerAction action;
    private final @Nullable T value;
    private final JsonSerializationFunction<T> serializer;

    public Container(Iterable<T> contents, ContainerAction action, T value, JsonSerializationFunction<T> serializer) {
        this(action, value, serializer);
        contents.forEach(this::register);
    }

    @Override
    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray(size());
        forEach(v -> array.add(serializer.serialize(v)));
        object.add("values", array);
        if (action != null) {
            object.add("action", ContainerAction.PARSER.serialize(action));
        }

        if (value != null) {
            object.add("value", serializer.serialize(value));
        }

        return object;
    }

    public static <V extends Nameable> Container<V> fromJson(JsonElement element, JsonParser<V> parser) {
        JsonObject object = element.getAsJsonObject();
        Set<V> values = new HashSet<>();
        JsonArray array = object.has("values") ? object.get("values").getAsJsonArray() : new JsonArray();
        array.forEach(v -> values.add(parser.deserialize(v)));
        ContainerAction action = object.has("action") ? ContainerAction.PARSER.deserialize(object.get("action")) : null;
        V value = object.has("value") ? parser.deserialize(object.get("value")) : null;
        return new Container<>(values, action, value, parser);
    }

    public static Container<Nameable> of(String... values) {
        Set<Nameable> set = Arrays.stream(values).map(NameableImpl::new).collect(Collectors.toSet());
        return new Container<>(set, null, null, Parsers.NAME);
    }

}
