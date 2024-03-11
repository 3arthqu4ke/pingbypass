package me.earth.pingbypass.server.session;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.api.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Manages {@link Subscriber}s that should only be subscribed as long as a {@link Session} is connected.
 */
@RequiredArgsConstructor
public class SessionSubscriberService {
    private final AtomicBoolean shutdown = new AtomicBoolean();
    private final List<Subscriber> subscribers = new ArrayList<>();
    private final EventBus eventBus;

    /**
     * Adds a subscriber and subscribes it, as long as this service has not been shutdown.
     *
     * @param subscriber the subscriber to subscribe.
     */
    @Synchronized
    public void addAndSubscribe(Subscriber subscriber) {
        if (!shutdown.get()) {
            subscribers.add(subscriber);
            eventBus.subscribe(subscriber);
        }
    }

    /**
     * Unsubscribes all subscribers and shuts this service down.
     */
    @Synchronized
    public void shutdown() {
        shutdown.set(true);
        subscribers.forEach(eventBus::unsubscribe);
        subscribers.clear();
    }

}
