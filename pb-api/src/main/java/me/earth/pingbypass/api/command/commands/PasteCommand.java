package me.earth.pingbypass.api.command.commands;

import com.google.gson.JsonElement;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.AbstractPbCommand;
import me.earth.pingbypass.api.command.CommandSource;
import me.earth.pingbypass.api.command.impl.arguments.JsonArgument;
import me.earth.pingbypass.api.command.impl.arguments.ModuleArgument;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.config.impl.configs.ParsesSettingRegistries;
import me.earth.pingbypass.api.module.Module;
import net.minecraft.client.Minecraft;

public class PasteCommand extends AbstractPbCommand implements ParsesSettingRegistries {
    public PasteCommand(PingBypass pingBypass, Minecraft minecraft) {
        super("paste", "Paste the config of a module.", pingBypass, minecraft);
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arg("module", new ModuleArgument(pingBypass.getModuleManager()))
                    .then(arg("json", new JsonArgument()).executes(ctx -> {
                        Module module = ctx.getArgument("module", Module.class);
                        JsonElement json = ctx.getArgument("json", JsonElement.class);
                        try {
                            fromJson(module, json);
                        } catch (ConfigParseException e) {
                            error("Failed to load config: " + e.getMessage());
                        }
                    })));
    }

}
