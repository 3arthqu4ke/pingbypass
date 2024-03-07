package me.earth.pingbypass.api.command.commands;

import com.google.gson.JsonElement;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.FindsArgument;
import me.earth.pingbypass.api.command.impl.arguments.ModuleArgument;
import me.earth.pingbypass.api.command.impl.arguments.NameableArgumentTypeImpl;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.impl.ConfigTypes;
import me.earth.pingbypass.api.config.impl.configs.AbstractSettingConfig;
import me.earth.pingbypass.api.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static me.earth.pingbypass.api.config.JsonSerializable.GSON;

// TODO: would be cool if this could be part of the config command?
public class CopyCommand extends AbstractPbCommand implements FindsArgument {
    public CopyCommand(PingBypass pingBypass, Minecraft minecraft) {
        super("copy", "Copies the config of a module to your clipboard.", pingBypass, minecraft);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        var arg = arg("module", new ModuleArgument(pingBypass.getModuleManager()));
        executesWithOptionalArguments(ctx -> {
                Module module = ctx.getArgument("module", Module.class);
                ConfigType configType = getArgument(ctx, "config", ConfigType.class, ConfigTypes.SETTINGS);
                var config = pingBypass.getConfigManager().getByConfigType(configType);
                if (config.isEmpty() || !(config.get() instanceof AbstractSettingConfig)) {
                    error("Failed to get config of type %s!".formatted(configType.getName()));
                    return;
                }

                JsonElement element = ((AbstractSettingConfig<?>) config.get()).toJson(module);
                mc.keyboardHandler.setClipboard(GSON.toJson(element));
                print(Component.literal("Copied the %s config of %s to your clipboard.".formatted(
                        config.get().getName(), module.getName())));
            }, arg,
            NameableArgumentTypeImpl.builder("config", ConfigTypes.BINDS, ConfigTypes.STYLE, ConfigTypes.SETTINGS));
        builder.then(arg);
    }

}
