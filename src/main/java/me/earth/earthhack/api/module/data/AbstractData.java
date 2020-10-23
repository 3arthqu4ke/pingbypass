package me.earth.earthhack.api.module.data;

import me.earth.earthhack.api.config.Preset;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractData<M extends Module> implements ModuleData
{
    protected final Map<Setting<?>, String> descriptions = new HashMap<>();
    protected final Set<Preset> presets = new LinkedHashSet<>();
    protected final M module;

    public AbstractData(M module)
    {
        this.module = module;
    }

    @Override
    public Map<Setting<?>, String> settingDescriptions()
    {
        return descriptions;
    }

    @Override
    public Collection<Preset> getPresets()
    {
        return presets;
    }

}
