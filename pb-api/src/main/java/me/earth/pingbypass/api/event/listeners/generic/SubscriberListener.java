package me.earth.pingbypass.api.event.listeners.generic;

import lombok.Getter;
import me.earth.pingbypass.api.event.api.EventListener;
import me.earth.pingbypass.api.event.api.Subscriber;

import java.util.Collections;
import java.util.List;

public abstract class SubscriberListener<E> extends Listener<E> implements Subscriber {
    @Getter
    private final List<EventListener<?>> listeners = Collections.singletonList(this);

}
