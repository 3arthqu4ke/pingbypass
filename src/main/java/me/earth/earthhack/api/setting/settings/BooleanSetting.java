package me.earth.earthhack.api.setting.settings;

import com.google.gson.JsonElement;
import me.earth.earthhack.api.setting.Setting;

public class BooleanSetting extends Setting<Boolean>
{
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public BooleanSetting(String nameIn, Boolean initialValue)
    {
        super(nameIn, initialValue);
    }

    @Override
    public void fromJson(JsonElement element)
    {
        setValue(element.getAsBoolean());
    }

    @Override
    public boolean fromString(String string)
    {
        if ("true".equalsIgnoreCase(string))
        {
            this.setValue(true);
            return true;
        }
        else if ("false".equalsIgnoreCase(string))
        {
            this.setValue(false);
            return true;
        }

        return false;
    }

    @Override
    public String getInputs(String string)
    {
        if (string == null || string.isEmpty())
        {
            return "<true/false>";
        }

        if (TRUE.startsWith(string.toLowerCase()))
        {
            return TRUE;
        }
        else if (FALSE.startsWith(string.toLowerCase()))
        {
            return FALSE;
        }

        return "";
    }

}
