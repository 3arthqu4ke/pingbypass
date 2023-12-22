package me.earth.pingbypass.api.event.listeners.generic;

import lombok.Getter;
import me.earth.pingbypass.api.event.api.EventListener;

/**
 * The documentation of {@link Listener} gives an example of what forms of implementation do not work.
 * This class is for that purpose.
 *
 * @param <E> the type of event handled by this Listener.
 */
@Getter
public abstract class GenericListener<E> implements EventListener<E> {
    private final Class<E> type;
    private final Class<?> genericType;
    private final int priority;

    @SuppressWarnings("unchecked")
    public GenericListener(Class<?> type, int priority) {
        this.genericType = TypeHelper.getTypeInfo(this.getClass()).type();
        this.priority = priority;
        this.type = (Class<E>) type;
    }

}
