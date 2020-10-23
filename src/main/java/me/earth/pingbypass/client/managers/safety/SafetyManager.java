package me.earth.pingbypass.client.managers.safety;

import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.util.thread.ThreadUtil;
import me.earth.pingbypass.client.modules.safety.Safety;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SafetyManager extends SubscriberImpl implements Globals
{
    private static final SafetyManager INSTANCE = new SafetyManager();

    private final AtomicBoolean safe = new AtomicBoolean(false);

    private SafetyManager()
    {
        this.listeners.add(new TickListener(this));
        this.listeners.add(new GameLoopListener(this));
    }

    public static SafetyManager getInstance()
    {
        return INSTANCE;
    }

    public boolean isSafe()
    {
        return safe.get();
    }

    public void setSafe(boolean safeIn)
    {
        this.safe.set(safeIn);
    }

    protected void runThread()
    {
        if (mc.player != null && mc.world != null)
        {
            List<EntityPlayer> players = new ArrayList<>(mc.world.playerEntities);
            List<Entity> crystals      = new ArrayList<>(mc.world.loadedEntityList);
            boolean newerVersion       = Safety.getInstance().newerVersion();
            boolean safetyPlayer       = Safety.getInstance().safetyPlayer();
            boolean bedCheck           = Safety.getInstance().shouldBedCheck();
            float maximalDamage        = Safety.getInstance().getMaxDamage();

            SafetyRunnable runnable = new SafetyRunnable(this, players, crystals, newerVersion, safetyPlayer, bedCheck, maximalDamage);
            ThreadUtil.run(runnable);
        }
    }

}
