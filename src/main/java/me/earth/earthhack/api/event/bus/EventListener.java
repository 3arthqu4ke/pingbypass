package me.earth.earthhack.api.event.bus;

import me.earth.earthhack.api.event.bus.api.Listener;

/**
 * Implementation of the Listener interface.
 *
 * @param <T> type of object this listener listens to.
 */
public abstract class EventListener<T> implements Listener<T>
{
    /** Targeted class */
    private final Class<? super T> target;
    /** Type of targeted class */
    private final Class<?> type;
    /** Priority of this listener */
    private final int priority;

    public EventListener(Class<? super T> target)
    {
        this(target, 10, null);
    }

    public EventListener(Class<? super T> target, Class<?> type)
    {
        this(target, 10, type);
    }

    public EventListener(Class<? super T> target, int priority)
    {
        this(target, priority, null);
    }

    public EventListener(Class<? super T> target, int priority, Class<?> type)
    {
        this.priority = priority;
        this.target   = target;
        this.type     = type;
    }

    @Override
    public int getPriority()
    {
        return priority;
    }

    @Override
    public Class<? super T> getTarget()
    {
        return target;
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }

}
