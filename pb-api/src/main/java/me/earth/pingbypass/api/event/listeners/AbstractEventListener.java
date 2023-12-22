package me.earth.pingbypass.api.event.listeners;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.event.api.EventListener;

@Getter
@RequiredArgsConstructor
public abstract class AbstractEventListener<E> implements EventListener<E> {
    private final Class<E> type;
    private final Class<?> genericType;
    private final int priority;

    public AbstractEventListener(Class<E> type) {
        this(type, null, DEFAULT_LISTENER_PRIORITY);
    }

    public AbstractEventListener(Class<E> type, int priority) {
        this(type, null, priority);
    }

    public AbstractEventListener(Class<E> type, Class<?> genericType) {
        this(type, genericType, DEFAULT_LISTENER_PRIORITY);
    }

    public static abstract class Unsafe<E> extends AbstractEventListener<E> {
        public Unsafe(Class<?> type) {
            this(type, DEFAULT_LISTENER_PRIORITY);
        }

        public Unsafe(Class<?> type, int priority) {
            this(type, null, priority);
        }

        public Unsafe(Class<?> type, Class<?> genericType) {
            this(type, genericType, DEFAULT_LISTENER_PRIORITY);
        }

        @SuppressWarnings("unchecked")
        public Unsafe(Class<?> type, Class<?> genericType, int priority) {
            super((Class<E>) type, genericType, priority);
        }
    }

}
