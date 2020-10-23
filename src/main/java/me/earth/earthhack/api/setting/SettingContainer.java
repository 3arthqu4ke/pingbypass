package me.earth.earthhack.api.setting;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SettingContainer extends Nameable
{
    private final Map<String, Setting<?>> settings = new LinkedHashMap<>();

    protected SettingContainer(String name)
    {
        super(name);
        register(super.displayName);
    }

    public <T> Setting<T> register(Setting<T> setting)
    {
        setting.setContainer(this);
        settings.put(setting.getName().toLowerCase(), setting);
        return setting;
    }

    public <T, S extends Setting<T>> S unregister(Setting<T> setting)
    {
        return (S) settings.remove(setting.getName().toLowerCase());
    }

    public <T, S extends Setting<T>> S getSetting(String name)
    {
        return (S) settings.get(name.toLowerCase());
    }

    public void resetSettings()
    {
        for (Setting<?> setting : settings.values())
        {
            setting.reset();
        }
    }

    public Collection<Setting<?>> getSettings()
    {
        return settings.values();
    }

}
