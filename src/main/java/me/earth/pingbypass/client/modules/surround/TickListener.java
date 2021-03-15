package me.earth.pingbypass.client.modules.surround;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.TickEvent;

public class TickListener extends ModuleListener<Surround, TickEvent>
{
    protected TickListener(Surround module)
    {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event)
    {
        module.onTick();
    }

}