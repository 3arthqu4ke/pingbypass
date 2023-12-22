package me.earth.pingbypass.api.event.api;

import org.jetbrains.annotations.NotNull;

public interface EventListener<E> extends EventConsumer<E>, Comparable<EventListener<E>> {
    int DEFAULT_LISTENER_PRIORITY = 0;

    Class<E> getType();

    default Class<?> getGenericType() {
        return null;
    }

    default int getPriority() {
        return DEFAULT_LISTENER_PRIORITY;
    }

    @Override
    default int compareTo(@NotNull EventListener<E> o) {
        return Integer.compare(this.getPriority(), o.getPriority());
    }

}
