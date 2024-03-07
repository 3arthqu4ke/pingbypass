package me.earth.pingbypass.api.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.arguments.ModuleArgument;
import me.earth.pingbypass.api.module.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ToggleCommand extends AbstractPbCommand {
    public ToggleCommand(PingBypass pingBypass, Minecraft mc) {
        super("toggle", "Toggles a module.", pingBypass, mc);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("module", new ModuleArgument(pingBypass.getModuleManager())).executes(ctx -> {
            Module module = ctx.getArgument("module", Module.class);
            module.toggle();
            print(Component.literal("Toggling ")
                            .withStyle(module.isEnabled() ? ChatFormatting.GREEN : ChatFormatting.RED)
                            .append(Component.literal(module.getName())
                                    .withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD)), module.getName());
        }));
    }

}
