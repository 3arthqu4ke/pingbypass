package me.earth.earthhack.api.setting;

import me.earth.earthhack.api.setting.settings.StringSetting;

public class Nameable
{
    final Setting<String> displayName;

    protected Nameable(String name)
    {
        this.displayName = new StringSetting("Name", name);
    }

    public String getName()
    {
        return displayName.getInitial();
    }

    public String getDisplayName()
    {
        return displayName.getValue();
    }

    public void setDisplayName(String name)
    {
        this.displayName.setValue(name);
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Nameable && ((Nameable) obj).getName().equals(this.getName());
    }

    @Override
    public int hashCode()
    {
        return displayName.getInitial().hashCode();
    }

}
