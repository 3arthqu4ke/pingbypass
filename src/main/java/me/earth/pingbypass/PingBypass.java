package me.earth.pingbypass;

import me.earth.pingbypass.client.managers.CommandManager;
import me.earth.pingbypass.client.managers.ConfigManager;
import me.earth.pingbypass.client.managers.EventManager;
import me.earth.pingbypass.client.managers.ModuleManager;
import me.earth.pingbypass.client.modules.servermodule.ServerModule;
import me.earth.pingbypass.server.PhobosClient;
import me.earth.pingbypass.server.PhobosServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = PingBypass.MODID, name = PingBypass.NAME, version = PingBypass.VERSION)
public class PingBypass
{
    public static final String MODID = "pingbypass";
    public static final String NAME = "PingBypass";
    public static final String VERSION = "1.0.0";

    public static Logger logger;

    public static PhobosClient client;
    public static PhobosServer server;

    public static ConfigManager configManager;
    public static ModuleManager moduleManager;
    public static CommandManager commandManager;
    public static EventManager eventManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        client = new PhobosClient();

        moduleManager  = new ModuleManager();
        commandManager = new CommandManager();
        configManager  = new ConfigManager();
        eventManager   = new EventManager();

        configManager.load();
        eventManager.init();

        server = new PhobosServer(ServerModule.getInstance().getIP(), ServerModule.getInstance().getPort());
    }

}
