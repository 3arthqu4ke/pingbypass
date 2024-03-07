package me.earth.pingbypass.client.input;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.event.SubscriberImpl;
import me.earth.pingbypass.api.event.listeners.generic.Listener;
import me.earth.pingbypass.api.input.Bind;
import me.earth.pingbypass.api.input.Key;
import me.earth.pingbypass.api.input.KeyboardAndMouse;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.event.impl.KeyEvent;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class KeybindManager extends SubscriberImpl {
    public KeybindManager(PingBypass pingBypass) {
        KeyboardAndMouse keyboardAndMouse = pingBypass.getKeyBoardAndMouse();
        Minecraft mc = pingBypass.getMinecraft();
        listen(new Listener<KeyEvent>() {
            @Override
            public void onEvent(KeyEvent event) {
                if (event.getAction() == GLFW.GLFW_PRESS && mc.screen == null) {
                    Key key = keyboardAndMouse.getKeyByCode(event.getType(), event.getKey());
                    if (key != null) {
                        for (Module module : pingBypass.getModuleManager()) {
                            // TODO: make access to Bind faster
                            Optional<Setting<Bind>> optionalBindSetting = module.getSetting("Bind", Bind.class);
                            optionalBindSetting.ifPresent(bindSetting -> {
                                Bind bind = bindSetting.getValue();
                                if (bind.getKeys().contains(key)) {
                                    if (bind.getKeys().size() == 1 || keyboardAndMouse.isPressed(bind)) {
                                        module.toggle();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

}
