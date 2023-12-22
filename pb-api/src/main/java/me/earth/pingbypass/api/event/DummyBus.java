package me.earth.pingbypass.api.event;

import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.api.EventListener;

public enum DummyBus implements EventBus {
    INSTANCE;

    @Override
    public void post(Object event) {
        // NOP
    }

    @Override
    public void post(Object event, Class<?> generic) {
        // NOP
    }

    @Override
    public void subscribe(Object subscriber) {
        // NOP
    }

    @Override
    public void unsubscribe(Object subscriber) {
        // NOP
    }

    @Override
    public boolean isSubscribed(Object subscriber) {
        return false;
    }

    @Override
    public void unregister(EventListener<?> listener) {
        // NOP
    }


}
