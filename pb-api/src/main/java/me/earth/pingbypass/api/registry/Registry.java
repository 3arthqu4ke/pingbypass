package me.earth.pingbypass.api.registry;

import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;

import java.util.Optional;

// TODO: Namespace for better conflict solving?!
// TODO: Could we build this on top of Minecraft registries instead?
public interface Registry<T extends Nameable> extends Streamable<T> {
    boolean register(T object);

    boolean unregister(T object);

    Optional<T> getByName(String name);

    default boolean contains(String name) {
        return getByName(name).isPresent();
    }

    default boolean contains(T object) {
        return stream().anyMatch(object::equals);
    }

    // TODO: iterator can remove too!!!!!
    // TODO: this is unsafe!
    default void clear() {
        this.stream().forEach(this::unregister);
    }

    @SuppressWarnings("unchecked")
    default <C extends T> Optional<C> getByClass(String name, Class<C> clazz) {
        Optional<T> byName = this.getByName(name);
        if (clazz.isInstance(byName.orElse(null))) {
            return (Optional<C>) byName;
        }

        return Optional.empty();
    }

    default int size() {
        return (int) stream().count();
    }

}
