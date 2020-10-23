package me.earth.pingbypass.client.managers;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.pingbypass.client.managers.safety.SafetyManager;

/**
 * Subscribes all managers to the EventBus.
 */
public class EventManager
{
    public void init()
    {
        Bus.EVENT_BUS.subscribe(SwitchManager.getInstance());
        Bus.EVENT_BUS.subscribe(RotationManager.getInstance());
        Bus.EVENT_BUS.subscribe(new VelocityManager());
        Bus.EVENT_BUS.subscribe(SafetyManager.getInstance());
    }
}
