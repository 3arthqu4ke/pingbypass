package me.earth.pingbypass.api.input;

import me.earth.pingbypass.api.traits.Streamable;
import org.jetbrains.annotations.Nullable;

public interface KeyboardAndMouse extends Streamable<Key> {
    boolean isPressed(Key.Type keyType, int keyCode);

    @Nullable Key getKeyByCode(Key.Type type, int keyCode);

    @Nullable Key getKeyByName(String name);

    default boolean isPressed(Key key) {
        return isPressed(key.getType(), key.getCode());
    }

    default boolean isPressed(Bind bind) {
        return !bind.getKeys().isEmpty() && bind.stream().allMatch(this::isPressed);
    }

}
