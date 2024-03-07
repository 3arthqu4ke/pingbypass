package me.earth.pingbypass.api.launch;

import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.plugin.PluginConfig;
import me.earth.pingbypass.api.side.Side;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Launches {@link Side#SERVER} and/or {@link Side#CLIENT}, or any other Side, if their jar is in the "pingbypass/plugins" folder.
 */
@Slf4j
public class SideLaunchingPlugin implements SimpleMixinConfigPlugin {
    private static final AtomicBoolean loaded = new AtomicBoolean();

    public SideLaunchingPlugin() {
        synchronized (loaded) {
            if (!loaded.getAndSet(true)) {
                log.info("Loading SideLaunchingPlugin...");
                PreLaunchService preLaunch = PreLaunchServiceImpl.INSTANCE;
                var discovery = new PluginDiscoveryServiceImpl(preLaunch.getPlatformProvider(), new Path[]{preLaunch.getRootFileManager().mkdirs("plugins")}, Side.ANY) {
                    @Override
                    protected boolean checkSideProvider(PluginConfig config) {
                        return config.getProvides() != null && config.getProvides().length > 0;
                    }
                };

                discovery.load();
            }
        }
    }

}
