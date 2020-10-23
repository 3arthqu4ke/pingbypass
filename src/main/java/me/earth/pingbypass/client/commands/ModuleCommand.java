package me.earth.pingbypass.client.commands;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.impl.util.text.TextColor;
import me.earth.pingbypass.PingBypass;

public class ModuleCommand extends AbstractCommand
{
    public ModuleCommand()
    {
        super("module");
    }

    @Override
    public void execute(String[] args)
    {
        if (args.length == 3)
        {
            String moduleName  = args[0];
            String settingName = args[1];
            String valueName   = args[2];

            Module module = PingBypass.moduleManager.getModule(moduleName);
            if (module != null)
            {
                Setting<?> setting = module.getSetting(settingName);
                if (setting != null)
                {
                    if (setting.getName().equalsIgnoreCase("Enabled"))
                    {
                        if (valueName.equalsIgnoreCase("true"))
                        {
                            module.enable();
                        }
                        else if (valueName.equalsIgnoreCase("false"))
                        {
                            module.disable();
                        }

                        return;
                    }
                    else
                    {
                        if (setting.fromString(valueName))
                        {
                            return;
                        }
                    }
                }
            }

            PingBypass.server.sendToClient(TextColor.RED + "Error occurred: " + moduleName + ", " + settingName + ", " + valueName);
            return;
        }

        PingBypass.server.sendToClient(TextColor.RED + "Try <module> <setting> <value>.");
    }

}
