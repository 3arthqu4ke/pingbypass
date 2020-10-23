package me.earth.earthhack.api.util;

public class EnumHelper
{

    public static Enum<?> next(Enum<?> entry)
    {
        Enum<?>[] array = entry.getDeclaringClass().getEnumConstants();
        return array.length - 1 == entry.ordinal() ? array[0] : array[entry.ordinal() + 1];
    }

    public static Enum<?> previous(Enum<?> entry)
    {
        Enum<?>[] array = entry.getDeclaringClass().getEnumConstants();
        return entry.ordinal() == 0 ? array[array.length - 1] : array[entry.ordinal() - 1];
    }

    public static Enum<?> fromString(Enum<?> initial, String name)
    {
        Class<? extends Enum<?>> clazz = initial.getDeclaringClass();
        for (Enum<?> constant : clazz.getEnumConstants())
        {
            if (constant.name().equalsIgnoreCase(name))
            {
                return constant;
            }
        }

        return initial;
    }

}
