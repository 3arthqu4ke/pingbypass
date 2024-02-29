package me.earth.pingbypass.client.mixins;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.impl.KeyEvent;
import me.earth.pingbypass.api.input.Key;
import net.minecraft.client.KeyboardHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void keyPressHook(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
            KeyEvent.MAIN_THREAD_INSTANCE.setKey(key);
            KeyEvent.MAIN_THREAD_INSTANCE.setAction(action);
            KeyEvent.MAIN_THREAD_INSTANCE.setType(Key.Type.KEYBOARD);
            PingBypassApi.getEventBus().post(KeyEvent.MAIN_THREAD_INSTANCE);
        }
    }
    
}
