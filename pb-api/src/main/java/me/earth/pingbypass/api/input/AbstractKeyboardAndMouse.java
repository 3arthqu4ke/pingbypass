package me.earth.pingbypass.api.input;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class AbstractKeyboardAndMouse implements KeyboardAndMouse {
    private final KeyRegistry keys;

    @Override
    public @Nullable Key getKeyByCode(Key.Type type, int keyCode) {
        return keys.getKeyByCode(type, keyCode);
    }

    @Override
    public @Nullable Key getKeyByName(String name) {
        return keys.getByName(name).orElse(null);
    }

    @NotNull
    @Override
    public Iterator<Key> iterator() {
        return keys.iterator();
    }

    @Override
    public Stream<Key> stream() {
        return keys.stream();
    }

}
