package me.earth.pingbypass.client.mixins;

import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.input.Key;
import me.earth.pingbypass.api.event.impl.KeyEvent;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler {
    @Inject(method = "onPress", at = @At("HEAD"))
    private void onPressHook(long windowPointer, int button, int action, int modifiers, CallbackInfo ci) {
        if (button != GLFW.GLFW_KEY_UNKNOWN) {
            KeyEvent.MAIN_THREAD_INSTANCE.setKey(button);
            KeyEvent.MAIN_THREAD_INSTANCE.setAction(action);
            KeyEvent.MAIN_THREAD_INSTANCE.setType(Key.Type.MOUSE);
            PingBypassApi.getEventBus().post(KeyEvent.MAIN_THREAD_INSTANCE);
        }
    }

}
