package me.earth.earthhack.api.event.events;

/**
 * A simple Event, containing a
 * boolean property "cancelled".
 */
public abstract class Event
{
    /** The cancellation state of this Event, */
    boolean cancelled;

    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

}

