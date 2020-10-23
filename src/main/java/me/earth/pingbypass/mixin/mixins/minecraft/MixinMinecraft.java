package me.earth.pingbypass.mixin.mixins.minecraft;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.event.events.GameLoopEvent;
import me.earth.earthhack.impl.event.events.TickEvent;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.modules.fakeplayer.FakePlayer;
import me.earth.pingbypass.client.modules.servermodule.ServerModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft
{
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", ordinal = 0, shift = At.Shift.AFTER))
    private void post_ScheduledTasks(CallbackInfo callbackInfo)
    {
        Bus.EVENT_BUS.post(new GameLoopEvent());
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void runTickHook(CallbackInfo info)
    {
        Bus.EVENT_BUS.post(new TickEvent());
    }

    @Inject(method = "shutdownMinecraftApplet", at = @At(value = "HEAD"), cancellable = true)
    private void shutdownMinecraftAppletHook(CallbackInfo callbackInfo)
    {
        if (PingBypass.configManager != null)
        {
            ServerModule.getInstance().getSetting("NoRender").setValue(false);
            FakePlayer.getInstance().disable();

            try
            {
                PingBypass.configManager.save();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

}
