package me.earth.pingbypass.client.modules.servermodule;

import me.earth.earthhack.api.module.Category;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;

public class ServerModule extends Module
{
    private static final ServerModule INSTANCE = new ServerModule();

    private final Setting<String> iServerIP = register(new StringSetting("IServerIP", "127.0.0.0"));
    private final Setting<String> port      = register(new StringSetting("Port", "0"));
    private final Setting<Boolean> noRender = register(new BooleanSetting("NoRender", false));

    private ServerModule()
    {
        super("PingBypass", Category.Client);
    }

    public static ServerModule getInstance()
    {
        return INSTANCE;
    }

    public String getIP()
    {
        return iServerIP.getValue();
    }

    public int getPort()
    {
        try
        {
            return Integer.parseInt(port.getValue());
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean shouldNoRender()
    {
        return noRender.getValue();
    }

}
