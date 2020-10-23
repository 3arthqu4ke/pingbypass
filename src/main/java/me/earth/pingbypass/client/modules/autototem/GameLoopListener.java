package me.earth.pingbypass.client.modules.autototem;

import me.earth.earthhack.impl.event.ModuleListener;
import me.earth.earthhack.impl.event.events.GameLoopEvent;
import me.earth.earthhack.impl.util.minecraft.EntityUtil;

public class GameLoopListener extends ModuleListener<AutoTotem, GameLoopEvent>
{
    protected GameLoopListener(AutoTotem module)
    {
        super(module, GameLoopEvent.class);
    }

    @Override
    public void invoke(GameLoopEvent event)
    {
        if (mc.player != null && module.isDangerous(EntityUtil.getHealth(mc.player)) && !module.badScreen())
        {
            module.doAutoTotem();
        }
    }

}

