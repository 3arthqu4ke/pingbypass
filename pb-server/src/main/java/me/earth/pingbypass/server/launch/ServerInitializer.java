package me.earth.pingbypass.server.launch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.command.impl.CommandManagerImpl;
import me.earth.pingbypass.api.command.impl.GenericCommandManagerImpl;
import me.earth.pingbypass.api.config.impl.ConfigManagerImpl;
import me.earth.pingbypass.api.event.impl.PingBypassInitializedEvent;
import me.earth.pingbypass.api.event.listeners.generic.SubscriberListener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleManagerImpl;
import me.earth.pingbypass.api.players.impl.PlayerRegistryImpl;
import me.earth.pingbypass.api.plugin.impl.PluginManagerImpl;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.command.ChatImpl;
import me.earth.pingbypass.api.event.ShutdownEvent;
import me.earth.pingbypass.api.event.loop.TickEvent;
import me.earth.pingbypass.api.launch.InitializationService;
import me.earth.pingbypass.api.launch.Initializer;
import me.earth.pingbypass.api.launch.PreLaunchService;
import me.earth.pingbypass.api.security.SecurityManagerFactory;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConfig;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.commands.ServerCommandInitializer;
import me.earth.pingbypass.server.commands.api.ServerCommand;
import me.earth.pingbypass.server.commands.api.ServerCommandSource;
import me.earth.pingbypass.server.handlers.ServerConnectionListener;
import me.earth.pingbypass.server.input.ServerKeyboardAndMouse;
import me.earth.pingbypass.server.service.MaxPlayersService;
import me.earth.pingbypass.server.service.QueueService;
import me.earth.pingbypass.server.service.ServerStatusService;
import me.earth.pingbypass.server.session.AdminService;
import me.earth.pingbypass.server.session.SessionManager;
import net.minecraft.client.Minecraft;

@Slf4j
public class ServerInitializer implements Initializer {
    @Override
    @SneakyThrows
    public void init(Minecraft mc, PreLaunchService preLaunchService) {
        log.info("\n\nLoading PingBypass Server!");

        val queueService = new QueueService(mc);
        val sessionManager = new SessionManager();

        val fileManager = preLaunchService.getFileManager(Side.SERVER);
        val config = new ServerConfig(fileManager.mkFile("pingbypass.properties"));
        val securityManager = SecurityManagerFactory.fromConfig(fileManager.mkdirs("security"), config);

        val id = config.get(Constants.ID, "unknown");
        log.info("Server id: '%s'".formatted(id));

        val commandManager = new CommandManagerImpl();
        val moduleManager = new ModuleManagerImpl(new Categories());
        val configManager = new ConfigManagerImpl();
        val serverCommandManager = new GenericCommandManagerImpl<ServerCommandSource, ServerCommand>();

        val server = new PingBypassServer(
                PingBypassApi.getEventBus(),
                new ServerKeyboardAndMouse(),
                commandManager,
                moduleManager,
                configManager,
                fileManager,
                preLaunchService.getRootFileManager(),
                securityManager,
                new PluginManagerImpl(),
                new PlayerRegistryImpl(),
                new PlayerRegistryImpl(),
                new ServerStatusService(sessionManager, queueService, mc),
                serverCommandManager,
                new MaxPlayersService(),
                sessionManager,
                queueService,
                config,
                new AdminService(),
                mc,
                new ChatImpl(mc),
                id);
        server.registerInstance();

        new InitializationService(preLaunchService, server, server.getMinecraft()).init();
        new ServerCommandInitializer().init(server);
        initServer(server);
        PingBypassApi.getEventBus().post(new PingBypassInitializedEvent(server));
        log.info("Finished loading PingBypass Server!\n\n");
    }

    private void initServer(PingBypassServer server) {
        val listener = new ServerConnectionListener(server);
        server.getEventBus().subscribe(server.getQueueService());
        server.getEventBus().subscribe(server.getMaxPlayersService());
        server.getEventBus().subscribe(new SubscriberListener<TickEvent>() {
            @Override
            public void onEvent(TickEvent event) {
                server.getSessionManager().tick();
            }
        });
        server.getEventBus().subscribe(new SubscriberListener<ShutdownEvent>() {
            @Override
            public void onEvent(ShutdownEvent event) {
                listener.stop();
            }
        });

        // TODO: option to not start listener and then start it later via command!
        listener.startTcpServerListener(server.getServerConfig().getInetAddress(), server.getServerConfig().get(ServerConstants.PORT));
    }

}
