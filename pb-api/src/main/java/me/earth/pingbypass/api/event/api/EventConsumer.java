package me.earth.pingbypass.api.event.api;

public interface EventConsumer<E> {
    void onEvent(E event);

}
