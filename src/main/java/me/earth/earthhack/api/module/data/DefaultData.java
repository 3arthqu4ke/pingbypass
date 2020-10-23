package me.earth.earthhack.api.module.data;

import me.earth.earthhack.api.config.DefaultPreset;
import me.earth.earthhack.api.module.Module;

public class DefaultData<M extends Module> extends AbstractData<M>
{
    public DefaultData(M module)
    {
        super(module);
        this.presets.add(new DefaultPreset(module));
        this.descriptions.put(module.getSetting("Enabled"), "Enables this module.");
        this.descriptions.put(module.getSetting("Name"), "Name shown in the Arraylist.");
        this.descriptions.put(module.getSetting("Hidden"), "Decides if this module should show up in the Arraylist.");
        this.descriptions.put(module.getSetting("Bind"), "Keybind to toggle this module.");
    }

    @Override
    public int getColor()
    {
        return 0xffffffff;
    }

    @Override
    public String getDescription()
    {
        return "A module.";
    }

}
