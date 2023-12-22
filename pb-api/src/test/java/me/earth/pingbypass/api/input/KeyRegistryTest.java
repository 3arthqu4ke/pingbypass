package me.earth.pingbypass.api.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyRegistryTest {
    @Test
    public void testKeyRegistry() {
        KeyRegistry keys = new KeyRegistry().loadKeyDataBase();
        Key key = new Key("MOUSE_1", Key.Type.MOUSE, Keys.MOUSE_1);
        assertEquals(key, keys.getKeyByCode(Key.Type.MOUSE, Keys.MOUSE_1));
        key = new Key("E", Key.Type.KEYBOARD, Keys.KEY_E);
        assertEquals(key, keys.getKeyByCode(Key.Type.KEYBOARD, Keys.KEY_E));
    }

}
