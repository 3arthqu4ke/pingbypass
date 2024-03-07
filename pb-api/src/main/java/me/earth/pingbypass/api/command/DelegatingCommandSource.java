package me.earth.pingbypass.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.EmptySuggestionProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@MethodsReturnNonnullByDefault
public class DelegatingCommandSource implements CommandSource {
    @Getter(onMethod_ = {@NotNull}) // idk this does not seem to work? have to suppress warnings in ContextImpl
    private final Minecraft minecraft;
    @Getter
    private final PingBypass pingBypass;

    @Delegate
    @SuppressWarnings("unused")
    public SharedSuggestionProvider getSuggestionProvider() {
        LocalPlayer player = minecraft.player;
        if (player != null) {
            return player.connection.getSuggestionsProvider();
        }

        return EmptySuggestionProvider.INSTANCE;
    }

}
