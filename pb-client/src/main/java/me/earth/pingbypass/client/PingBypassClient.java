package me.earth.pingbypass.client;

import lombok.Getter;
import me.earth.pingbypass.AbstractPingBypass;
import me.earth.pingbypass.api.command.Chat;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.config.ConfigManager;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.input.KeyboardAndMouse;
import me.earth.pingbypass.api.module.ModuleManager;
import me.earth.pingbypass.api.players.PlayerRegistry;
import me.earth.pingbypass.api.plugin.PluginManager;
import me.earth.pingbypass.api.security.SecurityManager;
import me.earth.pingbypass.api.side.Side;
import net.minecraft.client.Minecraft;

@Getter
public class PingBypassClient extends AbstractPingBypass {
    public PingBypassClient(EventBus eventBus, KeyboardAndMouse keyBoardAndMouse, CommandManager commandManager,
                            ModuleManager moduleManager, ConfigManager configManager, FileManager fileManager,
                            FileManager rootFileManager, SecurityManager securityManager, PluginManager pluginManager,
                            PlayerRegistry friendManager, PlayerRegistry enemyManager, Minecraft minecraft, Chat chat) {
        super(eventBus, keyBoardAndMouse, commandManager, moduleManager, configManager, fileManager,
                rootFileManager, securityManager, pluginManager, friendManager, enemyManager, minecraft, chat, Side.CLIENT);
    }

}
