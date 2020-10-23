package me.earth.earthhack.impl.util.beans;

public class Wrapper<T>
{
    protected final T value;

    public Wrapper(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

}
