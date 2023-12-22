package me.earth.pingbypass.client.event;

import lombok.Data;
import me.earth.pingbypass.api.input.Key;

@Data
public class KeyEvent {
    public static final KeyEvent INSTANCE = new KeyEvent();
    private int key;
    private int action;
    private Key.Type type;

}
