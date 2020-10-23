package me.earth.earthhack.api.setting.settings;

import com.google.gson.JsonElement;
import me.earth.earthhack.api.setting.Setting;

import java.text.NumberFormat;
import java.text.ParseException;

@SuppressWarnings("unchecked")
public class NumberSetting<N extends Number> extends Setting<N>
{
    private final boolean restriction;
    private final String  description;
    private N max;
    private N min;

    public NumberSetting(String nameIn, N initialValue)
    {
        super(nameIn, initialValue);
        this.restriction = false;
        this.description = generateOutPut();
    }

    public NumberSetting(String nameIn, N initialValue, N min, N max)
    {
        super(nameIn, initialValue);
        this.min = min;
        this.max = max;
        this.restriction = true;
        this.description = generateOutPut();
    }

    @Override
    public void fromJson(JsonElement element)
    {
        setValue(numberToValue(element.getAsNumber()));
    }

    @Override
    public boolean fromString(String string)
    {
        String noComma = string.replace(',', '.');

        try
        {
            Number parsed = NumberFormat.getInstance().parse(noComma);
            N result = numberToValue(parsed);
            if (result != null && inBounds(result))
            {
                this.setValue(result);
                return true;
            }
        }
        catch (ParseException | ClassCastException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String getInputs(String string)
    {
        if (string == null || string.isEmpty())
        {
            return description;
        }

        return "";
    }

    @Override
    public void setValue(N value)
    {
        if (inBounds(value))
        {
            super.setValue(value);
        }
    }

    public void increment()
    {
        this.setValue(numberToValue(this.value.doubleValue() + 1));
    }

    public void decrement()
    {
        this.setValue(numberToValue(this.value.doubleValue() - 1));
    }

    public boolean inBounds(N value)
    {
        return !restriction || (!(value.doubleValue() < min.doubleValue()) && !(value.doubleValue() > max.doubleValue()));
    }

    public boolean hasRestriction()
    {
        return restriction;
    }

    public N getMax()
    {
        return max;
    }

    public N getMin()
    {
        return min;
    }

    public N numberToValue(Number number)
    {
        Class<? extends Number> type = this.initial.getClass();
        Object result = null;

        if (type == Integer.class)
        {
            result = number.intValue();
        }
        else if (type == Float.class)
        {
            result = number.floatValue();
        }
        else if (type == Double.class)
        {
            result = number.doubleValue();
        }
        else if (type == Short.class)
        {
            result = number.shortValue();
        }
        else if (type == Byte.class)
        {
            result = number.byteValue();
        }
        else if (type == Long.class)
        {
            result = number.longValue();
        }

        return (N) result;
    }

    private String generateOutPut()
    {
        if (restriction)
        {
            return "<" + min.toString() + " - " + max.toString() + ">";
        }
        else
        {
            return "<-5, 1.0, 10 ... 1337>";
        }
    }

}
