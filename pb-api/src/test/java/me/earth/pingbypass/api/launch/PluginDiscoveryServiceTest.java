package me.earth.pingbypass.api.launch;

import me.earth.pingbypass.api.platform.Platform;
import me.earth.pingbypass.api.plugin.PluginConfig;
import me.earth.pingbypass.api.plugin.PluginConfigContainer;
import me.earth.pingbypass.api.side.Side;
import me.earth.pingbypass.api.Constants;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PluginDiscoveryServiceTest {
    @Test
    public void testPluginDiscoveryService() {
        var discoveryService = new PluginDiscoveryServiceImpl(new DummyPlatformProvider(), new Path[0], Side.SERVER);
        System.setProperty(Constants.CLASSPATH_PLUGINS, "test.plugin.config.json");
        discoveryService.load();
        assertTrue(discoveryService.getPluginConfigs().findAny().isEmpty());
        assertNull(PluginDiscoveryServiceTestMixinConnector.instance);
        assertNull(PluginDiscoveryServiceTestMixinConnector.side);

        discoveryService = new PluginDiscoveryServiceImpl(new DummyPlatformProvider(), new Path[0], Side.CLIENT);
        discoveryService.load();
        assertNotNull(PluginDiscoveryServiceTestMixinConnector.instance);
        assertEquals(Side.CLIENT, PluginDiscoveryServiceTestMixinConnector.side);
        assertEquals(1, discoveryService.getPluginConfigs().count());
        PluginConfigContainer container = discoveryService.getPluginConfigs().findFirst().orElseThrow();
        assertEquals("TestPlugin", container.getName());
        assertNull(container.getPath());
        PluginConfig config = container.getConfig();
        assertEquals(config.getName(), container.getName());
        assertNull(config.getMixinConfig());
        assertEquals(PluginDiscoveryServiceTestMixinConnector.class.getName(), config.getMixinConnector());
        assertEquals("TestMainClass", config.getMainClass());
        assertEquals("TestDescription", config.getDescription());
        assertEquals("TestUrl", config.getUrl());
        assertArrayEquals(new Side[]{Side.CLIENT}, config.getSupports());
        assertArrayEquals(new String[]{"TestAuthor"}, config.getAuthors());
        assertArrayEquals(new Platform[]{Platform.FABRIC}, config.getPlatforms());
    }

    @Test
    public void testConfigWithDefaults() {
        var discoveryService = new PluginDiscoveryServiceImpl(new DummyPlatformProvider(), new Path[0], Side.SERVER);
        System.setProperty(Constants.CLASSPATH_PLUGINS, "test.config.defaults.json");
        discoveryService.load();

        PluginConfigContainer container = discoveryService.getPluginConfigs().findFirst().orElseThrow();
        assertEquals("TestPluginDefaults", container.getName());
        assertNull(container.getPath());
        PluginConfig config = container.getConfig();

        assertEquals(config.getName(), container.getName());
        assertNull(config.getMixinConfig());
        assertNull(config.getMixinConnector());
        assertNull(config.getMainClass());
        assertNull(config.getDescription());
        assertNull(config.getUrl());

        assertArrayEquals(new Side[]{Side.CLIENT, Side.SERVER}, config.getSupports());
        assertArrayEquals(new String[]{"3arthqu4ke"}, config.getAuthors());
        assertArrayEquals(new Platform[]{Platform.FABRIC, Platform.FORGE}, config.getPlatforms());
    }

}
