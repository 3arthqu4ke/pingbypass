package me.earth.pingbypass.api.event.api;

public interface EventBus {
    void post(Object event);

    void post(Object event, Class<?> generic);

    void subscribe(Object subscriber);

    void unsubscribe(Object subscriber);

    boolean isSubscribed(Object subscriber);

    void unregister(EventListener<?> listener);

}
