package me.earth.pingbypass.client.managers;

import me.earth.earthhack.api.module.Module;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.client.commands.AbstractCommand;
import me.earth.pingbypass.client.commands.FriendCommand;
import me.earth.pingbypass.client.commands.ModuleCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Quick and dirty command system, as this isn't too important.
 * ModuleCommand is essentially all you need and should be
 * managed by the client.
 */
public class CommandManager
{
    private final Map<String, AbstractCommand> commands = new HashMap<>();

    public CommandManager()
    {
        for (Module module : PingBypass.moduleManager.getModules())
        {
            commands.put(module.getName().toLowerCase(), new ModuleCommand());
        }

        register(new FriendCommand());
    }

    public void onMessage(String message)
    {
        String[] parts = message.split(" ");
        AbstractCommand command = commands.get(parts[0].toLowerCase());
        if (command != null)
        {
            command.execute(parts);
        }
    }

    private void register(AbstractCommand command)
    {
        commands.put(command.getName().toLowerCase(), command);
    }

}
