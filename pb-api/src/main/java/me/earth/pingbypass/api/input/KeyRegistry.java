package me.earth.pingbypass.api.input;

import lombok.SneakyThrows;
import me.earth.pingbypass.api.registry.impl.SortedRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyRegistry extends SortedRegistry<Key> {
    private final Map<Key.Type, Map<Integer, Key>> lookup = new EnumMap<>(Key.Type.class);

    @Override
    protected boolean registerSynchronously(Key key) {
        if (super.registerSynchronously(key)) {
            lookup.computeIfAbsent(key.getType(), v -> new ConcurrentHashMap<>()).put(key.getCode(), key);
            return true;
        }

        return false;
    }

    @Override
    protected boolean unregisterSynchronously(Key key) {
        Map<Integer, Key> byId = lookup.get(key.getType());
        if (byId != null) {
            byId.remove(key.getCode(), key);
        }

        return super.unregisterSynchronously(key);
    }

    public @Nullable Key getKeyByCode(Key.Type type, int keyCode) {
        return lookup.getOrDefault(type, Collections.emptyMap()).get(keyCode);
    }

    @SneakyThrows
    public KeyRegistry loadKeyDataBase() {
        KeyRegistry registry = new KeyRegistry();
        for (Field field : Keys.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != int.class) {
                continue;
            }

            String name = field.getName();
            Key.Type type = null;
            if (name.startsWith(Keys.KEY_PREFIX)) {
                name = name.substring(Keys.KEY_PREFIX.length());
                type = Key.Type.KEYBOARD;
            } else if (name.startsWith(Keys.MOUSE_PREFIX)) {
                type = Key.Type.MOUSE;
            }

            if (type != null) {
                field.setAccessible(true);
                int code = (int) field.get(null);
                registry.register(new Key(getNameForKey(type, name, code), type, code));
            }
        }

        return registry;
    }

    protected String getNameForKey(Key.Type type, String name, int code) {
        return name;
    }

}
