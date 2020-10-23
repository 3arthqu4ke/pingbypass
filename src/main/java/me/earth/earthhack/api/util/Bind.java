package me.earth.earthhack.api.util;

import org.lwjgl.input.Keyboard;

public class Bind
{
    private int key;

    public Bind(int key)
    {
        this.key = key;
    }

    public int getKey()
    {
        return key;
    }

    public void setKey(int key)
    {
        this.key = key;
    }

    public static Bind none()
    {
        return new Bind(-1);
    }

    public static Bind fromString(String string)
    {
        return new Bind(Keyboard.getKeyIndex(string));
    }

    @Override
    public String toString()
    {
        return key < 0 ? "NONE" : Keyboard.getKeyName(key);
    }

}
