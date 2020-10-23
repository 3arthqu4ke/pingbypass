package me.earth.pingbypass.client.modules.autocrystal;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.TickEvent;
import me.earth.earthhack.impl.util.thread.ThreadUtil;
import me.earth.pingbypass.client.modules.autocrystal.modes.Rotate;

public class TickListener extends ModuleListener<AutoCrystal, TickEvent>
{
    protected TickListener(AutoCrystal module)
    {
        super(module, TickEvent.class);
    }

    @Override
    public void invoke(TickEvent event)
    {
        if (mc.player != null && mc.world != null && module.rotate.getValue() == Rotate.None && module.tick.getValue())
        {
            ThreadUtil.run(() -> module.onTick(null));
        }
    }

}
