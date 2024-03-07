package me.earth.pingbypass.client.launch;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.command.impl.CommandManagerImpl;
import me.earth.pingbypass.api.config.impl.ConfigManagerImpl;
import me.earth.pingbypass.api.config.properties.PropertyConfigImpl;
import me.earth.pingbypass.api.event.impl.PingBypassInitializedEvent;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleManagerImpl;
import me.earth.pingbypass.api.players.impl.PlayerRegistryImpl;
import me.earth.pingbypass.api.plugin.impl.PluginManagerImpl;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.client.PingBypassClient;
import me.earth.pingbypass.client.input.GLFWKeyboardAndMouse;
import me.earth.pingbypass.client.input.KeybindManager;
import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.command.ChatImpl;
import me.earth.pingbypass.api.launch.InitializationService;
import me.earth.pingbypass.api.launch.Initializer;
import me.earth.pingbypass.api.launch.PreLaunchService;
import me.earth.pingbypass.api.security.SecurityManagerFactory;
import net.minecraft.client.Minecraft;

@Slf4j
public class ClientInitializer implements Initializer {
    @Override
    public void init(Minecraft mc, PreLaunchService preLaunchService) {
        log.info("\n\nLoading PingBypass Client!");

        val fileManager = preLaunchService.getFileManager(Side.CLIENT);
        val config = PropertyConfigImpl.tryReadPath(fileManager.mkFile("pingbypass.properties"));
        val securityManager = SecurityManagerFactory.fromConfig(fileManager.mkdirs("security"), config);

        val id = config.get(Constants.ID, "unknown");
        log.info("Client id: '%s'".formatted(id));

        val client = new PingBypassClient(
                PingBypassApi.getEventBus(),
                new GLFWKeyboardAndMouse(mc.getWindow().getWindow()),
                new CommandManagerImpl(),
                new ModuleManagerImpl(new Categories()),
                new ConfigManagerImpl(),
                fileManager,
                preLaunchService.getRootFileManager(),
                securityManager,
                new PluginManagerImpl(),
                new PlayerRegistryImpl(),
                new PlayerRegistryImpl(),
                mc,
                new ChatImpl(mc)
                // id);
        );
        client.registerInstance();

        new InitializationService(preLaunchService, client, client.getMinecraft()).init();
        client.getEventBus().subscribe(new KeybindManager(client));
        PingBypassApi.getEventBus().post(new PingBypassInitializedEvent(client));
        log.info("Finished loading PingBypass Client!\n\n");
    }

}
