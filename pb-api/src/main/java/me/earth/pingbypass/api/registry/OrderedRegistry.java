package me.earth.pingbypass.api.registry;

import me.earth.pingbypass.api.traits.Nameable;

public interface OrderedRegistry<T extends Nameable> extends Registry<T> {
    boolean registerBefore(T object, T before);

    boolean registerAfter(T object, T after);

    boolean registerFirst(T object);

}
