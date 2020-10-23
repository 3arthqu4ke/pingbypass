package me.earth.earthhack.impl.util.math;

public class StopWatch
{
    private long time;

    public boolean passed(double ms)
    {
        return System.nanoTime() - time >= ms * 1000000;
    }

    public boolean passed(long ms)
    {
        return System.nanoTime() - time >= ms * 1000000;
    }

    public StopWatch reset()
    {
        time = System.nanoTime();
        return this;
    }

    public long getTime()
    {
        return (System.nanoTime() - time) / 1000000;
    }

}
