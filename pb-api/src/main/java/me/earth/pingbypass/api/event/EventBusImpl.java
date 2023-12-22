package me.earth.pingbypass.api.event;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class EventBusImpl implements EventBus {
    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, List<EventListener>> listeners =
        new ConcurrentHashMap<>();
    private final Set<Subscriber> subscribers = Collections.newSetFromMap(
        new ConcurrentHashMap<>());

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void post(Object event) {
        List<EventListener> listenersForEvent = listeners.get(event.getClass());
        if (listenersForEvent != null) {
            for (EventListener listener : listenersForEvent) {
                listener.onEvent(event);
            }
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void post(Object event, Class<?> generic) {
        List<EventListener> listenersForEvent = listeners.get(event.getClass());
        if (listenersForEvent != null) {
            for (EventListener listener : listenersForEvent) {
                if (listener.getGenericType() == null
                    || listener.getGenericType() == generic) {
                    listener.onEvent(event);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void subscribe(Object object) {
        if (object instanceof Subscriber subscriber) {
            // TODO: Better way to get a lock for a subscriber?
            synchronized (object instanceof Lockable lock ? lock.getLock(this) : subscribers) {
                if (subscribers.add(subscriber)) {
                    subscriber.getListeners().forEach(l -> {
                        List<EventListener> ls = this.listeners.computeIfAbsent(
                            l.getType(), k -> new CopyOnWriteArrayList<>());
                        insertListener(l, ls);
                    });
                }
            }
        } else {
            throw new IllegalArgumentException(object + " is not a Subscriber");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void unsubscribe(Object object) {
        if (object instanceof Subscriber subscriber) {
            // TODO: Better way to get a lock for a subscriber?
            synchronized (object instanceof Lockable lock ? lock.getLock(this) : subscribers) {
                if (subscribers.remove(subscriber)) {
                    subscriber.getListeners().forEach(l -> {
                        List<EventListener> ls = listeners.get(l.getType());
                        if (ls != null) {
                            ls.remove(l);
                        }
                    });
                }
            }
        } else {
            throw new IllegalArgumentException(object + " is not a Subscriber");
        }
    }

    @Override
    public boolean isSubscribed(Object object) {
        if (object instanceof Subscriber subscriber) {
            // TODO: Better way to get a lock for a subscriber?
            synchronized (object instanceof Lockable lock ? lock.getLock(this) : subscribers) {
                return subscribers.contains(subscriber);
            }
        } else {
            throw new IllegalArgumentException(object + " is not a Subscriber");
        }
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void unregister(EventListener<?> listener) {
        synchronized (subscribers) {
            List<EventListener> listenersForEvent = listeners.get(listener.getType());
            if (listenersForEvent != null) {
                listenersForEvent.remove(listener);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void insertListener(EventListener<?> listener, List<EventListener> list) {
        int index = 0;
        while (index < list.size() && listener.compareTo(list.get(index)) < 0) {
            index++;
        }

        list.add(index, listener);
    }

}
