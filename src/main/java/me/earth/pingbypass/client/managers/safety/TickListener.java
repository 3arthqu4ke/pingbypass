package me.earth.pingbypass.client.managers.safety;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.TickEvent;
import me.earth.pingbypass.client.modules.safety.Safety;
import me.earth.pingbypass.client.modules.safety.UpdateMode;

public class TickListener extends EventListener<TickEvent>
{
    private final SafetyManager manager;

    protected TickListener(SafetyManager manager)
    {
        super(TickEvent.class);
        this.manager = manager;
    }

    @Override
    public void invoke(TickEvent event)
    {
        if (Safety.getInstance().getMode() == UpdateMode.Tick)
        {
            manager.runThread();
        }
    }

}
