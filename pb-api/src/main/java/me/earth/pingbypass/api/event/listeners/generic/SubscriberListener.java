package me.earth.pingbypass.api.event.listeners.generic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.earth.pingbypass.api.event.Lockable;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;

import java.util.Collections;
import java.util.List;

public abstract class SubscriberListener<E> extends Listener<E> implements Subscriber, Lockable {
    private final Object lock = new Object();
    @Getter
    private final List<EventListener<?>> listeners = Collections.singletonList(this);

    @Override
    public Object getLock(Object requester) {
        return lock;
    }

    /**
     * Listens for an Event once, then unsubscribes itself from the given {@link EventBus}.
     *
     * @param <E> the type of event this Listener is listening to.
     */
    @RequiredArgsConstructor
    public static abstract class OneTime<E> extends SubscriberListener<E> {
        private final EventBus eventBus;

        public abstract void onOneTimeEvent(E event);

        @Override
        public void onEvent(E event) {
            synchronized (getLock(eventBus)) {
                if (eventBus.isSubscribed(this)) {
                    onOneTimeEvent(event);
                    eventBus.unsubscribe(this);
                }
            }
        }
    }

}
