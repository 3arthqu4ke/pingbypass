package me.earth.pingbypass.util.thread;

import me.earth.earthhack.api.util.Globals;

/**
 * Util that helps with multithreading.
 */
public class ThreadUtil implements Globals
{
    /**
     * Convenience method that adds a scheduled
     * task that will be run only if mc.player != null;
     *
     * @param runnable task to run.
     */
    public static void addCheckedTask(Runnable runnable)
    {
        mc.addScheduledTask(() ->
        {
            if (mc.player != null)
            {
                runnable.run();
            }
        });
    }

    /**
     * Convenience method, runs a given runnable
     * immediately, if mc.player != null.
     *
     * @param runnable the runnable.
     */
    public static void runChecked(Runnable runnable)
    {
        if (mc.player != null)
        {
            runnable.run();
        }
    }

}
