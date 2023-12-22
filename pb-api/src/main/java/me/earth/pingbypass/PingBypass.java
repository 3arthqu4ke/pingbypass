package me.earth.pingbypass;

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

public interface PingBypass {
    EventBus getEventBus();

    KeyboardAndMouse getKeyBoardAndMouse();

    CommandManager getCommandManager();

    ModuleManager getModuleManager();

    ConfigManager getConfigManager();

    FileManager getFileManager();

    FileManager getRootFileManager();

    SecurityManager getSecurityManager();

    PluginManager getPluginManager();

    PlayerRegistry getFriendManager();

    PlayerRegistry getEnemyManager();

    Minecraft getMinecraft();

    Chat getChat();

    Side getSide();

}
