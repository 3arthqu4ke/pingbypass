package me.earth.earthhack.api.event.bus;

import me.earth.earthhack.api.event.bus.api.EventBus;
import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.earthhack.api.event.bus.api.Subscriber;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An EventBus implementation.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PhoBus implements EventBus
{
    /** Listeners, mapped to their Targets. */
    private final Map<Class<?>, List<Listener>> listeners = new ConcurrentHashMap<>();
    /** Subscribers in this set are subscribed. */
    private final Set<Subscriber> subscribers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /** Listener in this set are registered. */
    private final Set<Listener> subbedlisteners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public void post(Object object)
    {
        List<Listener> listening = listeners.get(object.getClass());
        if (listening != null)
        {
            listening.forEach(listener -> listener.invoke(object));
        }
    }

    @Override
    public void post(Object object, Class<?> type)
    {
        List<Listener> listening = listeners.get(object.getClass());
        if (listening != null)
        {
            for (Listener listener : listening)
            {
                if (listener.getType() == null || type == listener.getType())
                {
                    listener.invoke(object);
                }
            }
        }
    }

    @Override
    public void subscribe(Object object)
    {
        if (object instanceof Subscriber)
        {
            Subscriber subscriber = (Subscriber) object;
            for (Listener<?> listener : subscriber.getListeners())
            {
                register(listener);
            }

            subscribers.add(subscriber);
        }
    }

    @Override
    public void unsubscribe(Object object)
    {
        if (object instanceof Subscriber)
        {
            Subscriber subscriber = (Subscriber) object;
            for (Listener<?> listener : subscriber.getListeners())
            {
                unregister(listener);
            }

            subscribers.remove(subscriber);
        }
    }

    @Override
    public void register(Listener<?> listener)
    {
        if (subbedlisteners.add(listener))
        {
            addAtPriority(listener, listeners.computeIfAbsent(listener.getTarget(), v -> new CopyOnWriteArrayList<>()));
        }
    }

    @Override
    public void unregister(Listener<?> listener)
    {
        if (subbedlisteners.remove(listener))
        {
            List<Listener> list = listeners.get(listener.getTarget());
            if (list != null)
            {
                list.remove(listener);
            }
        }
    }

    @Override
    public boolean isSubscribed(Object object)
    {
        if (object instanceof Subscriber)
        {
            return subscribers.contains(object);
        }
        else if (object instanceof Listener)
        {
            return subbedlisteners.contains(object);
        }

        return false;
    }

    /**
     * Adds a given listener at the first index where
     * the other listeners priority is equal or smaller than
     * its priority.
     *
     * @param listener the listener to add.
     * @param list the list to add the listener to.
     */
    private void addAtPriority(Listener<?> listener, List<Listener> list)
    {
        int index = 0;
        while (index < list.size() && listener.getPriority() < list.get(index).getPriority())
        {
            index++;
        }

        list.add(index, listener);
    }

}
