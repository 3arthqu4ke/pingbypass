package me.earth.pingbypass.api.mixins;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.event.ShutdownEvent;
import me.earth.pingbypass.api.event.gui.GuiScreenEvent;
import me.earth.pingbypass.api.event.loop.GameloopEvent;
import me.earth.pingbypass.api.event.loop.TickEvent;
import me.earth.pingbypass.api.mixins.resource.IPackRepository;
import me.earth.pingbypass.api.platform.Platform;
import me.earth.pingbypass.api.platform.PlatformProvider;
import me.earth.pingbypass.api.resource.PackRepositoryHelper;
import me.earth.pingbypass.api.util.mixin.MixinHelper;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.server.packs.repository.PackRepository;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private PackRepository resourcePackRepository;

    @ModifyVariable(
        method = "runTick",
        //name = "i", // DONT USE, this can fail
        ordinal = 0,
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;runAllTasks()V",
            shift = At.Shift.AFTER))
    private int runTickHook(int i) {
        GameloopEvent.INSTANCE.setTicks(i);
        PingBypassApi.getEventBus().post(GameloopEvent.INSTANCE);
        return GameloopEvent.INSTANCE.getTicks();
    }

    @Inject(
        method = "<init>",
        at = @At(
            value = "me.earth.pingbypass.api.injectors.LenientAfterFieldAccess",
            opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/Minecraft;resourcePackRepository:Lnet/minecraft/server/packs/repository/PackRepository;"
            /* shift = At.Shift.AFTER is done by the Injection point, because shifting would wrap it */))
    private void resourcePackRepositoryHook(GameConfig config, CallbackInfo ci) {
        if (PlatformProvider.getInstance().getCurrent().equals(Platform.FABRIC)) {
            PackRepositoryHelper.addPingBypassRepositorySource(((IPackRepository) resourcePackRepository));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHook(CallbackInfo ci) {
        PingBypassApi.getEventBus().post(TickEvent.INSTANCE);
    }

    // TODO: are these good?
    @Inject(method = "run", at = @At("RETURN"))
    private void runHook(CallbackInfo ci) {
        PingBypassApi.getEventBus().post(new ShutdownEvent());
    }

    @Inject(method = "crash", at = @At("HEAD"))
    private static void crashHook(Minecraft mc, File file, CrashReport crashReport, CallbackInfo ci) {
        PingBypassApi.getEventBus().post(new ShutdownEvent());
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void setScreenHook(Screen screen, CallbackInfo ci) {
        MixinHelper.hook(new GuiScreenEvent<>(screen), screen == null ? null : screen.getClass(), ci);
    }

    @Inject(method = "setScreen", at = @At("RETURN"))
    private void setScreenHookReturn(Screen screen, CallbackInfo ci) {
        PingBypassApi.getEventBus().post(new GuiScreenEvent.Post<>(screen), screen == null ? null : screen.getClass());
    }

    @Inject(method = "createUserApiService", at = @At("HEAD"), cancellable = true)
    private void createUserApiServiceHook(YggdrasilAuthenticationService yggdrasilAuthenticationService, GameConfig gameConfig, CallbackInfoReturnable<UserApiService> cir) {
        if ("fabricmc".equalsIgnoreCase(gameConfig.user.user.getAccessToken()) || "0".equals(gameConfig.user.user.getAccessToken())) {
            LOGGER.info("Development environment detected, using offline auth.");
            cir.setReturnValue(UserApiService.OFFLINE);
        }
    }

}
