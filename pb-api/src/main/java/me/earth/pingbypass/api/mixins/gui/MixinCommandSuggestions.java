package me.earth.pingbypass.api.mixins.gui;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.event.chat.CommandSuggestionEvent;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestions.class)
public abstract class MixinCommandSuggestions {
    @Shadow @Final EditBox input;
    @Shadow @Nullable private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow @Nullable private CommandSuggestions.@Nullable SuggestionsList suggestions;
    @Shadow @Nullable private ParseResults<SharedSuggestionProvider> currentParse;
    @Shadow boolean keepSuggestions;

    @Unique
    private ParseResults<CommandSource> customParse;

    @Shadow
    protected abstract void updateUsageInfo();

    @Inject(
        method = "updateCommandInfo",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/components/CommandSuggestions;currentParse:Lcom/mojang/brigadier/ParseResults;",
            ordinal = 0),
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void updateCommandInfoHeadHook(CallbackInfo ci, String string) {
        if (this.customParse != null && !this.customParse.getReader().getString().equals(string)) {
            this.customParse = null;
        }
    }

    @Inject(
        method = "updateCommandInfo",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
            remap = false),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD)
    public void updateCommandInfoHook(CallbackInfo ci, String string, StringReader reader) {
        var event = new CommandSuggestionEvent(
                reader, keepSuggestions, input, suggestions, pendingSuggestions, currentParse, customParse, false);
        PingBypassApi.getEventBus().post(event);
        if (event.isCancelled()) {
            currentParse = event.getCurrentParse();
            customParse = event.getCustomParse();
            pendingSuggestions = event.getPendingSuggestions();
            if (pendingSuggestions != null && event.isUpdatingPendingSuggestions()) {
                pendingSuggestions.thenRun(() -> {
                    var pendingSuggestions = this.pendingSuggestions;
                    if (pendingSuggestions != null && pendingSuggestions.isDone()) {
                        updateUsageInfo();
                    }
                });
            }

            ci.cancel();
        }
    }

}
