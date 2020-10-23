package me.earth.pingbypass.client.managers.safety;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.impl.event.events.GameLoopEvent;
import me.earth.earthhack.impl.util.math.StopWatch;
import me.earth.pingbypass.client.modules.safety.Safety;
import me.earth.pingbypass.client.modules.safety.UpdateMode;

public class GameLoopListener extends EventListener<GameLoopEvent>
{
    private final StopWatch timer = new StopWatch();
    private final SafetyManager manager;

    protected GameLoopListener(SafetyManager manager)
    {
        super(GameLoopEvent.class);
        this.manager = manager;
    }

    @Override
    public void invoke(GameLoopEvent event)
    {
        if (Safety.getInstance().getMode() == UpdateMode.Fast && timer.passed(Safety.getInstance().getDelay()))
        {
            manager.runThread();
            timer.reset();
        }
    }

}
