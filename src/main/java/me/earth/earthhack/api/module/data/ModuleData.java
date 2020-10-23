package me.earth.earthhack.api.module.data;

import me.earth.earthhack.api.config.Preset;
import me.earth.earthhack.api.setting.Setting;

import java.util.Collection;
import java.util.Map;

public interface ModuleData
{

    /**
     * Returns the modules color.
     * The Color is used by the Arraylist for example.
     *
     * @return an ARGB value.
     */
    int getColor();

    /**
     * Returns the modules description.
     * Hopefully detailed!
     *
     * @return a description of the module.
     */
    String getDescription();

    /**
     * Returns Settings belonging to this module
     * mapped to a description.
     *
     * @return settings with their descriptions.
     */
    Map<Setting<?>, String> settingDescriptions();

    /**
     * Returns a collection of the modules presets.
     *
     * @return the modules presets.
     */
    Collection<Preset> getPresets();

}
