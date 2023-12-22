package me.earth.pingbypass.server.input;

import me.earth.pingbypass.api.input.Key;
import me.earth.pingbypass.api.input.KeyboardAndMouse;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

public class ServerKeyboardAndMouse implements KeyboardAndMouse {
    private final Map<Key.Type, Set<Integer>> pressed = new EnumMap<>(Key.Type.class);

    @Override
    public boolean isPressed(Key.Type keyType, int keyCode) {
        return pressed.getOrDefault(keyType, Collections.emptySet()).contains(keyCode);
    }

    @Override
    public Key getKeyByCode(Key.Type type, int keyCode) {
        return null;
    }

    @Override
    public Key getKeyByName(String name) {
        return null;
    }

    public void clear() {
        pressed.clear();
    }

    public void press(Key.Type type, int keyCode) {
        pressed.computeIfAbsent(type, v -> new HashSet<>()).add(keyCode);
    }

    public void unpress(Key.Type type, int keyCode) {
        Set<Integer> keys = pressed.get(type);
        if (keys != null) {
            keys.remove(keyCode);
        }
    }

    @Override
    public Stream<Key> stream() {
        return Stream.empty();
    }

    @NotNull
    @Override
    public Iterator<Key> iterator() {
        return Collections.emptyIterator();
    }

}
