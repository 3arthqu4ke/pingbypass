package me.earth.earthhack.api.module;

import me.earth.earthhack.api.event.bus.api.Listener;
import me.earth.earthhack.api.event.bus.api.Subscriber;
import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BindSetting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.util.Bind;
import me.earth.earthhack.api.util.Globals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A module.
 *
 * You're going to notice that I've designed
 * every module of the client as a singleton
 * because that's what they are. Only one instance
 * of each module should exist at the same time.
 */
public abstract class Module extends Hideable implements Globals, Subscriber
{
    protected final List<Listener<?>> listeners = new ArrayList<>();
    private final Setting<Boolean> enabled;
    private final Setting<Bind> bind;
    private final Category category;

    public Module(String name, Category category)
    {
        super(name);
        this.bind     = register(new BindSetting("Bind", Bind.none()));
        this.enabled  = register(new BooleanSetting("Enabled", false));
        this.category = category;
    }

    public final void toggle()
    {
        if (isEnabled())
        {
            disable();
        }
        else
        {
            enable();
        }
    }

    public final void enable()
    {
        if (!isEnabled())
        {
            enabled.setValue(true);
            onEnable();
            if (isEnabled()) //recheck if we disabled in onEnable.
            {
                Bus.EVENT_BUS.subscribe(this);
            }
        }
    }

    public final void disable()
    {
        if (isEnabled())
        {
            enabled.setValue(false);
            onDisable();
            if (!isEnabled()) //recheck if we enabled in onDisable.
            {
                Bus.EVENT_BUS.unsubscribe(this);
            }
        }
    }

    public final void load()
    {
        if (this.isEnabled() && !Bus.EVENT_BUS.isSubscribed(this))
        {
            Bus.EVENT_BUS.subscribe(this);
        }

        onLoad();
    }

    public final boolean isEnabled()
    {
        return enabled.getValue();
    }

    public String getDisplayInfo()
    {
        return null;
    }

    public Category getCategory()
    {
        return category;
    }

    public Bind getBind()
    {
        return bind.getValue();
    }

    protected void onEnable()
    {
        /* Implemented by the module */
    }

    protected void onDisable()
    {
        /* Implemented by the module */
    }

    protected void onLoad()
    {
        /* Implemented by the module */
    }

    @Override
    public Collection<Listener<?>> getListeners()
    {
        return listeners;
    }

}
