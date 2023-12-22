package me.earth.pingbypass.api.event;

import lombok.Getter;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class SubscriberImpl implements Subscriber, Lockable {
    @Getter
    protected final List<EventListener<?>> listeners = new ArrayList<>();
    private final Object lock = new Object();

    @Override
    public Object getLock(Object requester) {
        return lock;
    }

    protected void listen(EventListener<?> listener) {
        listeners.add(listener);
    }

    protected void listenToAll(Subscriber subscriber) {
        listeners.addAll(subscriber.getListeners());
    }

}
