package me.earth.pingbypass.server.mixins;

import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.launch.PreLaunchServiceImpl;
import me.earth.pingbypass.server.launch.ServerInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(
        method = "<init>",
        at = @At(
            value = "me.earth.pingbypass.api.injectors.LenientBeforeInvoke",
            target = "Lnet/minecraft/client/Minecraft;setOverlay(Lnet/minecraft/client/gui/screens/Overlay;)V"))
    public void initPingBypass(GameConfig gameConfig, CallbackInfo ci) {
        PreLaunchServiceImpl.INSTANCE.assertInitialized(Side.SERVER);
        new ServerInitializer().init(Minecraft.class.cast(this), PreLaunchServiceImpl.INSTANCE);
    }

}
