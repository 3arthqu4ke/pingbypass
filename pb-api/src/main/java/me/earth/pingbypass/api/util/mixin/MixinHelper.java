package me.earth.pingbypass.api.util.mixin;

import lombok.experimental.UtilityClass;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.event.CancellableEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Utility for {@link org.spongepowered.asm.mixin.Mixin}.
 */
@UtilityClass
public class MixinHelper {
    /**
     * Posts the given event on the {@link PingBypassApi#getEventBus()} and cancels the given {@link CallbackInfo} if
     * the event has been cancelled. Do not forget to mark your {@link org.spongepowered.asm.mixin.injection.Inject}
     * annotation as cancellable!
     *
     * @param event the event to post.
     * @param callbackInfo the CallbackInfo to cancel if the event has been cancelled.
     */
    public void hook(CancellableEvent event, CallbackInfo callbackInfo) {
        PingBypassApi.getEventBus().post(event);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    /**
     * Posts the given generic event on the {@link PingBypassApi#getEventBus()} via {@link EventBus#post(Object, Class)}
     * and cancels the given {@link CallbackInfo} if the event has been cancelled. Do not forget to mark your
     * {@link org.spongepowered.asm.mixin.injection.Inject} annotation as cancellable!
     *
     * @param event the event to post.
     * @param type the generic type of the event.
     * @param callbackInfo the CallbackInfo to cancel if the event has been cancelled.
     */
    public void hook(CancellableEvent event, @Nullable Class<?> type, CallbackInfo callbackInfo) {
        PingBypassApi.getEventBus().post(event, type);
        if (event.isCancelled()) {
            callbackInfo.cancel();
        }
    }

}
