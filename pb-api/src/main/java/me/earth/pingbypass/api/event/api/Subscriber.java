package me.earth.pingbypass.api.event.api;

import java.util.List;

public interface Subscriber {
    List<EventListener<?>> getListeners();

}
