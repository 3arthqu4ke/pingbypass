package me.earth.pingbypass.api.command.commands;

import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.api.command.CommandManager;
import net.minecraft.client.Minecraft;

public interface CommonCommandInitializer {
    default void registerCommonCommands(PingBypass pb, Minecraft mc) {
        CommandManager manager = pb.getCommandManager();
        manager.register(new ConfigCommand(pb, mc));
        manager.register(new CopyCommand(pb, mc));
        manager.register(new PasteCommand(pb, mc));
        manager.register(new EchoCommand(pb, mc));
        manager.register(new HelpCommand(pb));
        manager.register(new ModulesCommand(pb, mc));
        manager.register(new QuitCommand(pb, mc));
        manager.register(new ResetCommand(pb, mc));
        manager.register(new ToggleCommand(pb, mc));
        manager.register(new PluginsCommand(pb));

        manager.register(new PlayerRegistryCommand(
                "friend", pb.getMinecraft(), pb.getFriendManager(), "friended", "a friend", "Friends"));
        manager.register(new PlayerRegistryCommand(
                "enemy", pb.getMinecraft(), pb.getEnemyManager(), "enemied", "an enemy", "Enemies"));
    }

}
