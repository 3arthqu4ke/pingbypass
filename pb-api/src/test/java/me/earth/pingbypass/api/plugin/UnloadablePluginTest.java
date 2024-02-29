package me.earth.pingbypass.api.plugin;

import lombok.Getter;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.TestPingBypass;
import me.earth.pingbypass.api.module.impl.Categories;
import me.earth.pingbypass.api.module.impl.ModuleImpl;
import me.earth.pingbypass.api.plugin.impl.AbstractUnloadablePlugin;
import me.earth.pingbypass.api.plugin.impl.PluginUnloadingService;
import me.earth.pingbypass.api.setting.impl.types.BoolBuilder;
import me.earth.pingbypass.api.command.commands.QuitCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO: this should probably be in the API
public class UnloadablePluginTest {
    @Test
    public void testUnloadablePlugin() {
        PingBypass pingBypass = new TestPingBypass();
        var module = new ModuleImpl(pingBypass, "test", Categories.MOVEMENT, "") {};
        pingBypass.getModuleManager().register(module);
        assertNotNull(pingBypass.getModuleManager().getByName("test").orElse(null));
        assertNull(module.getByName("test").orElse(null));
        assertNull(pingBypass.getCommandManager().getByName("quit").orElse(null));
        assertNull(pingBypass.getModuleManager().getByName("test2").orElse(null));

        var unloadablePlugin = new UnloadablePlugin();
        unloadablePlugin.load(pingBypass);

        assertNotNull(module.getByName("test").orElse(null));
        assertNotNull(pingBypass.getModuleManager().getByName("test2").orElse(null));
        assertNotNull(pingBypass.getCommandManager().getByName("quit").orElse(null));
        var module2 = pingBypass.getModuleManager().getByName("test2").orElseThrow();
        assertTrue(pingBypass.getModuleManager().contains(module2));
        module2.enable();
        assertTrue(pingBypass.getEventBus().isSubscribed(module2));

        unloadablePlugin.unload();
        assertNull(pingBypass.getModuleManager().getByName("test2").orElse(null));
        assertNull(module.getByName("test").orElse(null));
        assertNull(pingBypass.getCommandManager().getByName("quit").orElse(null));
        assertFalse(pingBypass.getEventBus().isSubscribed(module2));
        assertFalse(pingBypass.getModuleManager().contains(module2));
        assertTrue(unloadablePlugin.isUnloaded());
    }

    @Getter
    private static final class UnloadablePlugin extends AbstractUnloadablePlugin {
        private boolean unloaded = false;

        @Override
        public void load(PingBypass pingBypass, PluginUnloadingService service) {
            assertTrue(service.registerCommand(new QuitCommand(pingBypass, null)));
            var module = new ModuleImpl(pingBypass, "test2", Categories.MOVEMENT, "") {};
            assertTrue(service.registerModule(module));
            assertTrue(service.registerSetting(pingBypass.getModuleManager().getByName("test").orElseThrow(),
                    new BoolBuilder().withName("test").withDescription("test").build()));
            assertFalse(service.registerModuleWithoutCommand(module));
            service.runOnUnload(() -> unloaded = true);
        }
    }

}
