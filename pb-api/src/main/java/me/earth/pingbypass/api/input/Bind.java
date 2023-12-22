package me.earth.pingbypass.api.input;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import lombok.Data;
import me.earth.pingbypass.api.config.JsonSerializable;
import me.earth.pingbypass.api.traits.Streamable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.stream.Stream;

@Data
public class Bind implements JsonSerializable, Streamable<Key> {
    private static final Bind NONE = new Bind();
    @Unmodifiable
    private final Set<Key> keys;

    public Bind(Key... keys) {
        this(new HashSet<>(Arrays.stream(keys).toList()));
    }

    public Bind(Set<Key> keys) {
        this.keys = Collections.unmodifiableSet(keys);
    }

    public static Bind fromJson(JsonElement element) {
        JsonArray array = element.getAsJsonArray();
        Set<Key> keys = new HashSet<>((int) (array.size() * 1.5));
        for (JsonElement je : array) {
            keys.add(Key.fromJson(je));
        }

        return new Bind(keys);
    }

    @Override
    public JsonElement toJson() {
        JsonArray array = new JsonArray(keys.size());
        for (Key key : keys) {
            if (key.getCode() != Keys.KEY_NONE) {
                array.add(key.toJson());
            }
        }

        return array;
    }

    @Override
    public Stream<Key> stream() {
        return keys.stream();
    }

    @Override
    public @NotNull Iterator<Key> iterator() {
        return keys.iterator();
    }

    public Component getComponent() {
        if (keys.isEmpty()) {
            return Component.literal("NONE").withStyle(ChatFormatting.GRAY);
        }

        MutableComponent component = Component.literal("");
        Iterator<Key> iterator = iterator();
        while (iterator.hasNext()) {
            Key key = iterator.next();
            component.append(Component.literal(key.getName()).withStyle(ChatFormatting.GRAY));
            if (iterator.hasNext()) {
                component.append(", ");
            }
        }

        return component;
    }

    public static Bind none() {
        return NONE;
    }

}
