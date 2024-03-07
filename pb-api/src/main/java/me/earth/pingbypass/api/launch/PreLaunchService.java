package me.earth.pingbypass.api.launch;

import me.earth.pingbypass.api.files.FileManager;
import me.earth.pingbypass.api.platform.PlatformProvider;
import me.earth.pingbypass.api.side.Side;

import java.util.stream.Stream;

public interface PreLaunchService {
    void init(Side side);

    boolean isInitialized(Side side);

    Transformer.Registry getTransformerRegistry();

    FileManager getRootFileManager();

    PluginDiscoveryService getPluginDiscoveryService(Side side);

    FileManager getFileManager(Side side);

    Stream<Side> getLoadedSides();

    PlatformProvider getPlatformProvider();

    default void assertInitialized(Side side) {
        if (!isInitialized(side)) {
            throw new IllegalStateException("PreMixinService has not been initialized for side " + side.getName());
        }
    }

}
