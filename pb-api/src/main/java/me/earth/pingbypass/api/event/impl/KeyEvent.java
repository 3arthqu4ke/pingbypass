package me.earth.pingbypass.api.event.impl;

import lombok.Data;
import me.earth.pingbypass.api.input.Key;

@Data
public class KeyEvent {
    public static final KeyEvent MAIN_THREAD_INSTANCE = new KeyEvent(false);
    private final boolean remote;
    private int key;
    private int action;
    private Key.Type type;

}
