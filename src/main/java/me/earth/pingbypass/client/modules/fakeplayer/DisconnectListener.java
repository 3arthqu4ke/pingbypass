package me.earth.pingbypass.client.modules.fakeplayer;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.DisconnectEvent;

public class DisconnectListener extends ModuleListener<FakePlayer, DisconnectEvent>
{
    public DisconnectListener(FakePlayer module)
    {
        super(module, DisconnectEvent.class);
    }

    @Override
    public void invoke(DisconnectEvent event)
    {
        module.disable();
    }

}
