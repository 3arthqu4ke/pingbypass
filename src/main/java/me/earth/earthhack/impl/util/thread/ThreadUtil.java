package me.earth.earthhack.impl.util.thread;

import me.earth.earthhack.api.util.Globals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil implements Globals
{
    public static void schedule(Runnable runnable)
    {
        mc.addScheduledTask(runnable);
    }

    public static void run(Runnable runnable)
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(runnable);
        executor.shutdown();
    }

}
