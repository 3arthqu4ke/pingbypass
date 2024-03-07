package me.earth.pingbypass.api.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.arguments.DynamicDescriptionArgument;
import me.earth.pingbypass.api.command.impl.arguments.ModuleArgument;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.setting.Setting;
import me.earth.pingbypass.api.setting.impl.SettingUtil;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ResetCommand extends AbstractPbCommand {
    public ResetCommand(PingBypass pingBypass, Minecraft mc) {
        super("reset", "Resets the settings of a module.", pingBypass, mc);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        DynamicDescriptionArgument.SavingArgumentType<Module> moduleArg = new DynamicDescriptionArgument.SavingArgumentType<>(new ModuleArgument(pingBypass.getModuleManager()));
        builder.then(arg("module", moduleArg).executes(ctx -> {
            Module module = ctx.getArgument("module", Module.class);
            print(Component.literal("Resetting ").withStyle(ChatFormatting.RED)
                    .append(Component.literal(module.getName()).withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD)));
            module.stream().forEach(setting -> SettingUtil.setValueUnchecked(setting, setting.getDefaultValue()));
        }).then(arg("setting", new DynamicDescriptionArgument<>(moduleArg, "setting")).executes(ctx -> {
            Module module = ctx.getArgument("module", Module.class);
            Setting<?> setting = ctx.getArgument("setting", Setting.class);
            print(Component.literal("Resetting ").withStyle(ChatFormatting.RED)
                    .append(Component.literal(module.getName()).withStyle(ChatFormatting.WHITE, ChatFormatting.BOLD))
                    .append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(setting.getName()).withStyle(ChatFormatting.AQUA)));
            SettingUtil.setValueUnchecked(setting, setting.getDefaultValue());
        })));
    }

}
