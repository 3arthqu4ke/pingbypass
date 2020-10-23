package me.earth.earthhack.api.setting.settings;

import com.google.gson.JsonElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.util.EnumHelper;

public class EnumSetting<E extends Enum<?>> extends Setting<E>
{
    private final String concatenated;

    public EnumSetting(String nameIn, E initialValue)
    {
        super(nameIn, initialValue);
        concatenated = concatenateInputs();
    }

    @Override
    public void fromJson(JsonElement element)
    {
        fromString(element.getAsString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean fromString(String string)
    {
        Enum<?> entry = EnumHelper.fromString(this.value, string);
        this.setValue((E) entry);
        return true;
    }

    @Override
    public String getInputs(String string)
    {
        if (string == null || string.isEmpty())
        {
            return concatenated;
        }

        Enum<?>[] array = initial.getDeclaringClass().getEnumConstants();
        for (Enum<?> entry : array)
        {
            if (entry.name().toLowerCase().startsWith(string.toLowerCase()))
            {
                return entry.toString();
            }
        }

        return "";
    }

    @SuppressWarnings("unchecked")
    public void next()
    {
        this.setValue((E) EnumHelper.next(this.value));
    }

    @SuppressWarnings("unchecked")
    public void previous()
    {
        this.setValue((E) EnumHelper.previous(this.value));
    }

    private String concatenateInputs()
    {
        StringBuilder builder = new StringBuilder("<");
        Class<? extends Enum<?>> clazz = this.initial.getDeclaringClass();
        for (Enum<?> entry : clazz.getEnumConstants())
        {
            builder.append(entry.name()).append(", ");
        }

        builder.replace(builder.length() - 2, builder.length(), ">");
        return builder.toString();
    }

}
