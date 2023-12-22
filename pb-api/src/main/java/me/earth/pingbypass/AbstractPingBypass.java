package me.earth.pingbypass;

import lombok.Getter;
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
public abstract class AbstractPingBypass implements PingBypass {
    private final EventBus eventBus;
    private final KeyboardAndMouse keyBoardAndMouse;
    private final CommandManager commandManager;
    private final ModuleManager moduleManager;
    private final ConfigManager configManager;
    private final FileManager fileManager;
    private final FileManager rootFileManager;
    private final SecurityManager securityManager;
    private final PluginManager pluginManager;
    private final PlayerRegistry friendManager;
    private final PlayerRegistry enemyManager;
    private final Minecraft minecraft;
    private final Chat chat;
    private final Side side;

    protected AbstractPingBypass(EventBus eventBus, KeyboardAndMouse keyBoardAndMouse, CommandManager commandManager,
                                 ModuleManager moduleManager, ConfigManager configManager, FileManager fileManager,
                                 FileManager rootFileManager, SecurityManager securityManager,
                                 PluginManager pluginManager, PlayerRegistry friendManager, PlayerRegistry enemyManager,
                                 Minecraft minecraft, Chat chat, Side side) {
        this.eventBus = eventBus;
        this.keyBoardAndMouse = keyBoardAndMouse;
        this.commandManager = commandManager;
        this.moduleManager = moduleManager;
        this.configManager = configManager;
        this.fileManager = fileManager;
        this.rootFileManager = rootFileManager;
        this.securityManager = securityManager;
        this.pluginManager = pluginManager;
        this.friendManager = friendManager;
        this.enemyManager = enemyManager;
        this.minecraft = minecraft;
        this.chat = chat;
        this.side = side;
    }

    public void registerInstance() {
        PingBypassApi.addInstance(this);
    }

}
