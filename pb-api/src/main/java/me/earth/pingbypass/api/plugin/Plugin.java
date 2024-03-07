package me.earth.pingbypass.api.plugin;

import me.earth.pingbypass.PingBypass;

// TODO: remap plugins in development environments!
public interface Plugin {
    /**
     * Loads this Plugin.
     */
    void load(PingBypass pingBypass);

    // TODO: add command for unloading
    // TODO: add function for reloading
    default void unload() {

    }

    default boolean supportsUnloading() {
        return false;
    }

}
