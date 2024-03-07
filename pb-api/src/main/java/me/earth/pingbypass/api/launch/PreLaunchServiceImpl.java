package me.earth.pingbypass.api.launch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.earth.pingbypass.api.Constants;
import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.files.FileManagerImpl;
import me.earth.pingbypass.api.platform.PlatformProvider;
import me.earth.pingbypass.api.side.Side;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
public enum PreLaunchServiceImpl implements PreLaunchService {
    INSTANCE;

    private final PlatformProvider platformProvider = PlatformProvider.getInstance();
    @Getter
    private final FileManager rootFileManager = new FileManagerImpl(Paths.get(Constants.NAME_LOW));
    @Getter
    private final Transformer.Registry transformerRegistry = getPlatformProvider().getPlatformService().injectTransformerRegistry();
    private final Map<Side, PluginDiscoveryService> pluginDiscoveryServices = new HashMap<>();
    private final Map<Side, FileManager> fileManagers = new HashMap<>();
    private final Set<Side> loaded = new HashSet<>();

    PreLaunchServiceImpl() {
        rootFileManager.mkdirs();
    }

    @Override
    public void init(Side side) {
        synchronized (loaded) {
            if (loaded.add(side)) {
                log.info("\n\nLoading PingBypass side '%s'...".formatted(side.getName()));
                log.debug("PingBypass directory: " + getRootFileManager().getRoot().toAbsolutePath());
                getFileManager(side).mkdirs();
                getPluginDiscoveryService(side).load();
                log.info("Done loading PingBypass side '%s'.\n".formatted(side.getName()));
            }
        }
    }

    @Override
    public boolean isInitialized(Side side) {
        return pluginDiscoveryServices.containsKey(side) && fileManagers.containsKey(side);
    }

    @Override
    public PluginDiscoveryService getPluginDiscoveryService(Side side) {
        return pluginDiscoveryServices.computeIfAbsent(side, k -> new PluginDiscoveryServiceImpl(
                platformProvider, new Path[]{getFileManager(side).mkdirs("plugins"), rootFileManager.mkdirs("plugins")},
                side));
    }

    @Override
    public FileManager getFileManager(Side side) {
        return fileManagers.computeIfAbsent(side, k -> rootFileManager.relative(side.getName()));
    }

    @Override
    public Stream<Side> getLoadedSides() {
        return fileManagers.keySet().stream();
    }

    @Override
    public PlatformProvider getPlatformProvider() {
        return platformProvider;
    }

}
