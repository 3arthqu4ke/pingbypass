package me.earth.pingbypass.client.managers;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.pingbypass.client.managers.listeners.EntityVelocityListener;
import me.earth.pingbypass.client.managers.listeners.ExplosionListener;

/**
 * We'll rather let the client manage Velocity,
 * so we cancel all packets accelerating us.
 */
public class VelocityManager extends SubscriberImpl
{
    public VelocityManager()
    {
        this.listeners.add(new EntityVelocityListener());
        this.listeners.add(new ExplosionListener());
    }
}
