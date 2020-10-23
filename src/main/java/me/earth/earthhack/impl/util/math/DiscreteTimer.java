package me.earth.earthhack.impl.util.math;

/**
 * A StopWatch that works
 * in environments with discrete delays(ticks).
 *
 * This can be used for accurate CPS settings for
 * example. Most clients I've seen have CPS settings
 * from 1-20 however in reality they either click at
 * 1/5/10/20 clicks due to the discrete nature of ticks.
 */
public interface DiscreteTimer
{
    /**
     * Returns if this timer has passed
     * the last delay set with reset.
     *
     * @param delay the delay.
     * @return <tt>true</tt> if passed.
     */
    boolean passed(long delay);

    /**
     * Resets this timer. Passed will return
     * <tt>true</tt> until the delay has been
     * passed.
     *
     * @param delay the delay.
     * @return this.
     */
    DiscreteTimer reset(long delay);

}
