package me.earth.earthhack.api.event.bus.instance;

import me.earth.earthhack.api.event.bus.PhoBus;
import me.earth.earthhack.api.event.bus.api.EventBus;

/**
 * Manages EventBus instances for convenient access.
 */
public class Bus
{

    /** An EventBus instance. */
    public static final EventBus EVENT_BUS = new PhoBus();

}
