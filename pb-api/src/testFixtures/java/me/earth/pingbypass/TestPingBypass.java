package me.earth.pingbypass;

import me.earth.pingbypass.api.command.impl.CommandManagerImpl;
import me.earth.pingbypass.api.config.impl.ConfigManagerImpl;
import me.earth.pingbypass.api.event.EventBusImpl;
import me.earth.pingbypass.api.files.FileManagerImpl;
import me.earth.pingbypass.api.input.DummyKeyboardAndMouse;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleManagerImpl;
import me.earth.pingbypass.api.players.impl.PlayerRegistryImpl;
import me.earth.pingbypass.api.plugin.impl.PluginManagerImpl;
import me.earth.pingbypass.api.side.Side;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPingBypass extends AbstractPingBypass {
    public TestPingBypass() {
        this(Paths.get(""));
    }

    public TestPingBypass(Path path) {
        super(new EventBusImpl(), DummyKeyboardAndMouse.INSTANCE, new CommandManagerImpl(),
                new ModuleManagerImpl(new Categories()), new ConfigManagerImpl(),
                new FileManagerImpl(path), new FileManagerImpl(path), new DummySecurityManager(),
                new PluginManagerImpl(), new PlayerRegistryImpl(), new PlayerRegistryImpl(), null, new DummyChat(),
                Side.CLIENT);
    }

}
