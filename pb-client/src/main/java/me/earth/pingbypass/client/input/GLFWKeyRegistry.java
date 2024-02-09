package me.earth.pingbypass.client.input;

import me.earth.pingbypass.api.input.Key;
import me.earth.pingbypass.api.input.KeyRegistry;
import me.earth.pingbypass.api.input.Keys;
import org.lwjgl.glfw.GLFW;

// TODO: key names are still not that satisfying, some keys are missing
// TODO: maybe just use Minecrafts keybind system?
public class GLFWKeyRegistry extends KeyRegistry {
    @Override
    protected String getNameForKey(Key.Type type, String name, int code) {
        String result = null;
        if (type == Key.Type.KEYBOARD && code != -1 && code != Keys.KEY_WORLD_1) {
            // TODO: invalidScancode warning on startup
            result = GLFW.glfwGetKeyName(code, -1);
        }

        return result == null ? name : result.toUpperCase();
    }

}
