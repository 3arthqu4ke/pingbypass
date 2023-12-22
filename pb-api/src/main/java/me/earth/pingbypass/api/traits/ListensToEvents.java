package me.earth.pingbypass.api.traits;

import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;

public interface ListensToEvents extends Subscriber {
    default void listen(EventListener<?> listener) {
        this.getListeners().add(listener);
    }

}
