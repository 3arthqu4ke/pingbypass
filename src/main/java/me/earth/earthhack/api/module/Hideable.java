package me.earth.earthhack.api.module;

import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.SettingContainer;
import me.earth.earthhack.api.setting.settings.BooleanSetting;

public class Hideable extends SettingContainer
{
    private final Setting<Boolean> hidden = register(new BooleanSetting("Hidden", false));

    public Hideable(String name)
    {
        super(name);
    }

    public void setHidden(boolean hidden)
    {
        this.hidden.setValue(hidden);
    }

    public boolean isHidden()
    {
        return hidden.getValue();
    }

}
