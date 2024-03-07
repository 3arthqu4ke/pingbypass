package me.earth.pingbypass.api.launch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.command.impl.module.ModuleCommand;
import me.earth.pingbypass.api.config.ConfigException;
import me.earth.pingbypass.api.config.ConfigManager;
import me.earth.pingbypass.api.config.impl.ConfigTypes;
import me.earth.pingbypass.api.config.impl.CurrentConfigManager;
import me.earth.pingbypass.api.config.impl.configs.BindConfig;
import me.earth.pingbypass.api.config.impl.configs.PlayerRegistryConfig;
import me.earth.pingbypass.api.config.impl.configs.SettingsConfig;
import me.earth.pingbypass.api.config.impl.configs.StyleConfig;
import me.earth.pingbypass.api.event.api.EventBus;
import me.earth.pingbypass.api.event.listeners.generic.SubscriberListener;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.module.Module;
import me.earth.pingbypass.api.command.CommandEventService;
import me.earth.pingbypass.api.command.DelegatingCommandSource;
import me.earth.pingbypass.api.command.commands.CommonCommandInitializer;
import me.earth.pingbypass.api.event.ShutdownEvent;
import me.earth.pingbypass.api.gui.TitleScreenService;
import me.earth.pingbypass.api.module.CommonModuleInitializer;
import me.earth.pingbypass.api.plugin.PluginInitializer;
import net.minecraft.client.Minecraft;

/**
 * Helps with initializing {@link PingBypass} instances. Registers common commands, modules, etc.
 */
@Slf4j
@RequiredArgsConstructor
public class InitializationService implements CommonCommandInitializer, CommonModuleInitializer {
    private final PreLaunchService preLaunchService;
    private final PingBypass pingBypass;
    private final Minecraft mc;

    public void init() {
        this.registerConfigs();
        this.registerServices();
        this.registerCommands();
        this.registerModules();

        this.initializePlugins();
        this.registerModuleCommands();

        this.loadConfigs();
    }

    public void registerConfigs() {
        ConfigManager configs = pingBypass.getConfigManager();
        FileManager fileManager = pingBypass.getFileManager();
        configs.register(new SettingsConfig<>(fileManager, pingBypass.getModuleManager()));
        configs.register(new BindConfig<>(fileManager, pingBypass.getModuleManager()));
        configs.register(new StyleConfig<>(fileManager, pingBypass.getModuleManager()));
        configs.register(new PlayerRegistryConfig(fileManager, ConfigTypes.FRIENDS, pingBypass.getFriendManager()));
        configs.register(new PlayerRegistryConfig(fileManager, ConfigTypes.ENEMIES, pingBypass.getEnemyManager()));
    }

    public void registerCommands() {
        this.registerCommonCommands(pingBypass, mc);
    }

    public void registerModules() {
        this.registerCommonModules(pingBypass);
    }

    public void registerServices() {
        EventBus bus = pingBypass.getEventBus();
        CommandManager commandManager = pingBypass.getCommandManager();
        bus.subscribe(new CommandEventService(commandManager, new DelegatingCommandSource(mc, pingBypass)));
        bus.subscribe(TitleScreenService.create(pingBypass));
    }

    public void initializePlugins() {
        new PluginInitializer(preLaunchService.getPluginDiscoveryService(pingBypass.getSide()),
                              preLaunchService.getPlatformProvider().getPlatformService(), pingBypass).init();
    }

    public void registerModuleCommands() {
        for (Module module : pingBypass.getModuleManager()) {
            pingBypass.getCommandManager().register(new ModuleCommand(module));
        }
    }

    public void loadConfigs() {
        CurrentConfigManager currentConfigManager = new CurrentConfigManager();
        try {
            currentConfigManager.fromFile(pingBypass.getFileManager().get("current.json"));
        } catch (ConfigException e) {
            log.warn("Failed to load current config", e);
        }

        pingBypass.getConfigManager().forEach(config -> {
            String current = currentConfigManager.getCurrentConfig(config);
            try {
                config.load(current);
            } catch (ConfigException e) {
                log.error("Failed to load '%s' for config '%s'".formatted(current, config.getName()), e);
            }
        });

        pingBypass.getEventBus().subscribe(new SubscriberListener<ShutdownEvent>() {
            @Override
            public void onEvent(ShutdownEvent event) {
                log.info("Saving PingBypass configs");
                pingBypass.getConfigManager().forEach(config -> {
                    try {
                        config.save();
                        currentConfigManager.setCurrentConfig(config, config.getCurrentConfig().getName());
                    } catch (ConfigException e) {
                        log.error("Failed to save the %s config!".formatted(config.getName()), e);
                    }
                });

                try {
                    currentConfigManager.toFile(pingBypass.getFileManager().get("current.json"));
                } catch (ConfigException e) {
                    log.error("Failed to save current config", e);
                }
            }
        });
    }

}
