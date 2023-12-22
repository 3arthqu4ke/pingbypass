package me.earth.pingbypass.api.registry;

import me.earth.pingbypass.api.traits.Streamable;

import java.util.Optional;

public interface GetsByClass<T> {
    <C extends T> Optional<C> getByClass(Class<C> clazz);

    interface UsingStream<T> extends GetsByClass<T>, Streamable<T> {
        @Override
        default <C extends T> Optional<C> getByClass(Class<C> clazz) {
            return stream().filter(clazz::isInstance).map(clazz::cast).findFirst();
        }
    }

}
