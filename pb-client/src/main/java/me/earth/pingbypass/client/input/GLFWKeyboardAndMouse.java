package me.earth.pingbypass.client.input;

import me.earth.pingbypass.api.input.AbstractKeyboardAndMouse;
import me.earth.pingbypass.api.input.Key;
import me.earth.pingbypass.api.input.Keys;
import org.lwjgl.glfw.GLFW;

public class GLFWKeyboardAndMouse extends AbstractKeyboardAndMouse {
    private final long window;

    public GLFWKeyboardAndMouse(long window) {
        super(new GLFWKeyRegistry().loadKeyDataBase());
        this.window = window;
    }

    @Override
    public boolean isPressed(Key.Type keyType, int keyCode) {
        if (keyType == Key.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(window, keyCode) == Keys.PRESS;
        } else {
            return GLFW.glfwGetKey(window, keyCode) == Keys.PRESS;
        }
    }

}
