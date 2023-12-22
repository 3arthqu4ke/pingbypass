package me.earth.pingbypass.server.launch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.command.impl.CommandManagerImpl;
import me.earth.pingbypass.api.config.impl.ConfigManagerImpl;
import me.earth.pingbypass.api.event.EventBusImpl;
import me.earth.pingbypass.api.event.impl.PingBypassInitializedEvent;
import me.earth.pingbypass.api.event.listeners.generic.SubscriberListener;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleManagerImpl;
import me.earth.pingbypass.api.players.impl.PlayerRegistryImpl;
import me.earth.pingbypass.api.plugin.impl.PluginManagerImpl;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.commons.Constants;
import me.earth.pingbypass.commons.command.ChatImpl;
import me.earth.pingbypass.commons.event.ShutdownEvent;
import me.earth.pingbypass.commons.event.loop.TickEvent;
import me.earth.pingbypass.commons.launch.InitializationService;
import me.earth.pingbypass.commons.launch.Initializer;
import me.earth.pingbypass.commons.launch.PreLaunchService;
import me.earth.pingbypass.security.SecurityManagerFactory;
import me.earth.pingbypass.server.PingBypassServer;
import me.earth.pingbypass.server.ServerConfig;
import me.earth.pingbypass.server.ServerConstants;
import me.earth.pingbypass.server.handlers.ServerConnectionListener;
import me.earth.pingbypass.server.input.ServerKeyboardAndMouse;
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
                sessionManager,
                queueService,
                config,
                new AdminService(),
                mc,
                new ChatImpl(mc),
                id);
        server.registerInstance();

        new InitializationService(preLaunchService, server, server.getMinecraft()).init();
        // initServer(server);
        PingBypassApi.getEventBus().post(new PingBypassInitializedEvent(server));
        log.info("Finished loading PingBypass Server!\n\n");
    }

    @SuppressWarnings("unused")
    private void initServer(PingBypassServer server) {
        val listener = new ServerConnectionListener(server);
        server.getEventBus().subscribe(server.getQueueService());
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

        listener.addChannel(server.getServerConfig().getInetAddress(),
                server.getServerConfig().get(ServerConstants.PORT));
    }

}
