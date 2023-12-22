package me.earth.pingbypass.api.traits;

public interface Toggleable {
    boolean isEnabled();

    void enable();

    void disable();

    default void toggle() {
        if (isEnabled()) {
            disable();
        } else {
            enable();
        }
    }

}
