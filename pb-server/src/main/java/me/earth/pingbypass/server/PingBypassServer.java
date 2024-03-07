package me.earth.pingbypass.server;

import lombok.Getter;
import me.earth.pingbypass.AbstractPingBypass;
import me.earth.pingbypass.api.command.Chat;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.command.GenericCommandManager;
import me.earth.pingbypass.api.config.ConfigManager;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.module.ModuleManager;
import me.earth.pingbypass.api.players.PlayerRegistry;
import me.earth.pingbypass.api.plugin.PluginManager;
import me.earth.pingbypass.api.security.SecurityManager;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.server.commands.api.ServerCommand;
import me.earth.pingbypass.server.commands.api.ServerCommandSource;
import me.earth.pingbypass.server.handlers.configuration.ConfigWorldStem;
import me.earth.pingbypass.server.input.ServerKeyboardAndMouse;
import me.earth.pingbypass.server.service.MaxPlayersService;
import me.earth.pingbypass.server.service.QueueService;
import me.earth.pingbypass.server.service.ServerStatusService;
import me.earth.pingbypass.server.session.AdminService;
import me.earth.pingbypass.server.session.SessionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SectionBufferBuilderPool;
import net.minecraft.server.WorldStem;

@Getter
public class PingBypassServer extends AbstractPingBypass {
    private final GenericCommandManager<ServerCommandSource, ServerCommand> serverCommandManager;
    private final ServerKeyboardAndMouse keyboardAndMouseAsServerKeyboard;
    private final ServerStatusService serverStatusService;
    private final MaxPlayersService maxPlayersService;
    private final SessionManager sessionManager;
    private final QueueService queueService;
    private final ServerConfig serverConfig;
    private final AdminService adminService;
    private final WorldStem worldStem;
    private final String id;

    public PingBypassServer(EventBus eventBus, ServerKeyboardAndMouse keyBoardAndMouse, CommandManager commandManager,
                            ModuleManager moduleManager, ConfigManager configManager, FileManager fileManager,
                            FileManager rootFileManager, SecurityManager securityManager, PluginManager pluginManager,
                            PlayerRegistry friendManager, PlayerRegistry enemyManager, ServerStatusService serverStatusService,
                            GenericCommandManager<ServerCommandSource, ServerCommand> serverCommandManager,
                            MaxPlayersService maxPlayersService, SessionManager sessionManager, QueueService queueService,
                            ServerConfig serverConfig, AdminService adminService, Minecraft minecraft, Chat chat, String id) {
        super(eventBus, keyBoardAndMouse, commandManager, moduleManager, configManager, fileManager,
                rootFileManager, securityManager, pluginManager, friendManager, enemyManager, minecraft, chat, Side.SERVER);
        this.keyboardAndMouseAsServerKeyboard = keyBoardAndMouse;
        this.serverCommandManager = serverCommandManager;
        this.maxPlayersService = maxPlayersService;
        this.worldStem = ConfigWorldStem.load(minecraft);
        this.serverStatusService = serverStatusService;
        this.sessionManager = sessionManager;
        this.queueService = queueService;
        this.serverConfig = serverConfig;
        this.adminService = adminService;
        this.id = id;
    }

}
