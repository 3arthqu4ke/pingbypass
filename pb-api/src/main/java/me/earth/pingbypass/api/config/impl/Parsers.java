package me.earth.pingbypass.api.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.pingbypass.api.config.JsonDeserializationFunction;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.config.JsonSerializationFunction;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.setting.impl.types.container.Container;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.NameableImpl;

import java.util.Collection;
import java.util.function.Supplier;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Parsers<T> implements JsonParser<T> {
    public static final JsonParser<Container<Nameable>> CONTAINER = new Parsers<>(e -> Container.fromJson(e, Parsers.NAME), Container::toJson);
    public static final JsonParser<Boolean> BOOL  = new Parsers<>(JsonElement::getAsBoolean, JsonPrimitive::new);
    public static final JsonParser<String> STRING = new Parsers<>(JsonElement::getAsString, JsonPrimitive::new);
    public static final JsonParser<Integer> INT   = new Parsers<>(JsonElement::getAsInt, JsonPrimitive::new);
    public static final JsonParser<Double> DOUBLE = new Parsers<>(JsonElement::getAsDouble, JsonPrimitive::new);
    public static final JsonParser<Float> FLOAT   = new Parsers<>(JsonElement::getAsFloat, JsonPrimitive::new);
    public static final JsonParser<Long> LONG     = new Parsers<>(JsonElement::getAsLong, JsonPrimitive::new);
    public static final JsonParser<Byte> BYTE     = new Parsers<>(JsonElement::getAsByte, JsonPrimitive::new);
    public static final JsonParser<Short> SHORT   = new Parsers<>(JsonElement::getAsShort, JsonPrimitive::new);
    public static final JsonParser<Bind> BINDS    = new Parsers<>(Bind::fromJson, Bind::toJson);

    public static final JsonParser<Nameable> NAME = new Parsers<>(NameableImpl::fromJson, NameableImpl::nameableToJson);

    @Delegate
    private final JsonDeserializationFunction<T> deserialize;
    @Delegate
    private final JsonSerializationFunction<T> serialize;

    public static <T extends Enum<T>> JsonParser<T> enumParser(Class<T> type) {
        return new Parsers<>(e -> Enum.valueOf(type, e.getAsString()), e -> new JsonPrimitive(e.name()));
    }

    public static <T, C extends Collection<T>> JsonParser<C> collection(JsonParser<T> parser, Supplier<C> factory) {
        return new Parsers<>(jsonElement -> {
            var result = factory.get();
            var array = jsonElement.getAsJsonArray();
            array.forEach(element -> result.add(parser.deserialize(element)));
            return result;
        }, collection -> {
            var result = new JsonArray(collection.size());
            collection.stream().map(parser::serialize).forEach(result::add);
            return result;
        });
    }

}
