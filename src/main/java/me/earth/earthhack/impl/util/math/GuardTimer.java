package me.earth.earthhack.impl.util.math;

/**
 * A simple implementation of the ITimer interface.
 * Using a StopWatch to prevent degeneration of the
 * timer after waiting for longer periods of time.
 *
 * I'm still not sure if I'm not just stupid and
 * there's a way easier way...
 */
public class GuardTimer implements DiscreteTimer
{
    private final StopWatch guard = new StopWatch();
    private long guardDelay;
    private long delay;
    private long time;

    public GuardTimer()
    {
        this(1000);
    }

    public GuardTimer(long guardDelay)
    {
        this.guardDelay = guardDelay;
    }

    @Override
    public boolean passed(long ms)
    {
        return System.nanoTime() - time >= ms * 1000000;
    }

    @Override
    public DiscreteTimer reset(long ms)
    {
        if (this.delay != ms || guard.passed(guardDelay))
        {
            this.delay = ms;
            reset();
        }
        else
        {
            time = ms * 1000000 + time;
        }

        return this;
    }

    /**
     * Sets the delay with which the timer gets
     * hard reset.
     *
     * @param guardDelay the new guardDelay.
     */
    public void setGuard(long guardDelay)
    {
        this.guardDelay = guardDelay;
    }

    /**
     * Hard resets this timer to System.nanoTime()
     * and the underlying guard StopWatch.
     */
    public void reset()
    {
        time = System.nanoTime();
        guard.reset();
    }

}
