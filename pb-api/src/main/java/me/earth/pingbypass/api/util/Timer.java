package me.earth.pingbypass.api.util;

import lombok.Getter;

@Getter
public class Timer {
    private long time;

    public boolean passed(double ms) {
        return getTimeMS() - time >= ms;
    }

    public boolean passed(long ms) {
        return getTimeMS() - time >= ms;
    }

    public void reset() {
        time = getTimeMS();
    }

    public long getTime() {
        return getTimeMS() - time;
    }

    public void setTime(long ns) {
        time = ns;
    }

    private long getTimeMS() {
        return System.nanoTime() / 1_000_000;
    }

}
