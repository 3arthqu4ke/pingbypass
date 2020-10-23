package me.earth.earthhack.impl.event.events;

import net.minecraft.util.text.ITextComponent;

public class DisconnectEvent
{
    private final ITextComponent component;

    public DisconnectEvent(ITextComponent component)
    {
        this.component = component;
    }

    public ITextComponent getComponent()
    {
        return component;
    }

}
