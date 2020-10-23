package me.earth.earthhack.api.util;

public class TextUtil
{

    public static boolean startsWithIgnoreCase(String string, String prefix)
    {
        return string.toLowerCase().startsWith(prefix.toLowerCase());
    }

    public static String safeSubstring(String string, int beginIndex)
    {
        if (string != null && beginIndex <= string.length())
        {
            return string.substring(Math.max(beginIndex, 0));
        }

        return "";
    }

    public static String safeSubstring(String string, int beginIndex, int endIndex)
    {
        if (string != null && endIndex > 0 && endIndex >= beginIndex)
        {
            return string.substring(Math.max(0, beginIndex), Math.min(endIndex, string.length()));
        }

        return "";
    }

}
