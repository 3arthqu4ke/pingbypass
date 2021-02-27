package me.earth.pingbypass.client.modules.surround;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.DisconnectEvent;

public class DisconnectListener extends ModuleListener<Surround, DisconnectEvent>
{
    public DisconnectListener(Surround module)
    {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void invoke(DisconnectEvent event)
    {
        module.disable();
    }

}

