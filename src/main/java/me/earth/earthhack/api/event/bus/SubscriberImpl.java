package me.earth.earthhack.api.event.bus;

import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.earthhack.api.event.bus.api.Subscriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple implementation of the Subscriber interface.
 */
public class SubscriberImpl implements Subscriber
{
    /**
     * Note that changes to these listeners
     * are not detected and require resubscribing
     * of the Subscriber after changes.
     */
    protected final List<Listener<?>> listeners = new ArrayList<>();

    @Override
    public Collection<Listener<?>> getListeners()
    {
        return listeners;
    }

}
