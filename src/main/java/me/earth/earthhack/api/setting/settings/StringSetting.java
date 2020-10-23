package me.earth.earthhack.api.setting.settings;

import com.google.gson.JsonElement;
import me.earth.earthhack.api.setting.Setting;

public class StringSetting extends Setting<String>
{
    public StringSetting(String nameIn, String initialValue)
    {
        super(nameIn, initialValue);
    }

    @Override
    public void fromJson(JsonElement element)
    {
        setValue(element.getAsString());
    }

    @Override
    public boolean fromString(String string)
    {
        return true;
    }

    @Override
    public String getInputs(String string)
    {
        if (string == null || string.isEmpty())
        {
            return "<name>";
        }

        return "";
    }

}
